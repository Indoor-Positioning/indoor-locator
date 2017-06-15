package com.mooo.sestus.indoor_locator.data;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

public class DeviceSensorRepository implements SensorRepository, SensorEventListener {
    private static SensorManager sensorManager;
    private static Sensor magneticSensorUncalibrated;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    private final float[] lastMeasurement = new float[7];
    private final float[] uncalibratedMagneticField = new float[3];
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];
    private static DeviceSensorRepository INSTANCE;
    private final WindowManager windowManager;
    private final Sensor magneticSensor;
    private final Sensor accelerometer;
    private final WifiManager wifiManager;
    private Callback callbacks;
    private final Object lock = new Object();

    public DeviceSensorRepository(Activity activity) {
        windowManager = activity.getWindowManager();
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        magneticSensorUncalibrated = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void register(Callback callbacks) {
        this.callbacks = callbacks;
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magneticSensorUncalibrated, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void deRegister() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public float[] getMeasurement() {
        synchronized (lock) {
            float wifiRssi = 0f;
            if (wifiManager.isWifiEnabled())
                wifiRssi = wifiManager.getConnectionInfo().getRssi();
            System.arraycopy(uncalibratedMagneticField, 0, lastMeasurement, 0, 3);
            System.arraycopy(mOrientationAngles, 0, lastMeasurement, 3, 3);
            lastMeasurement[6] = wifiRssi;
        }
        return lastMeasurement;
    }

    public static SensorRepository getInstance(Activity activity) {
        if (INSTANCE == null) {
            INSTANCE = new DeviceSensorRepository(activity);
        }
        return INSTANCE;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        }
        else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
            SensorManager.getRotationMatrix(mRotationMatrix, null,
                    mAccelerometerReading, mMagnetometerReading);
            computeOrientation(mRotationMatrix);
            SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
            synchronized (lock) {
                for (int i = 0; i < 3; i++) {
                    mOrientationAngles[i] = (float) (Math.toDegrees(mOrientationAngles[i]));
                }
            }
            float wifiRssi = 0f;
            if (wifiManager.isWifiEnabled())
                wifiRssi = wifiManager.getConnectionInfo().getRssi();
            if (callbacks != null)
                callbacks.onRotationSensorChanged(mOrientationAngles[0], mOrientationAngles[1], mOrientationAngles[2], wifiRssi);
        }
        else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
            synchronized (lock) {
                uncalibratedMagneticField[0] = event.values[0];
                uncalibratedMagneticField[1] = event.values[1];
                uncalibratedMagneticField[2] = event.values[2];
            }
            if (callbacks != null)
                callbacks.onMagneticSensorChanged(event.values[0], event.values[1], event.values[2]);
        }


    }

    private void computeOrientation(float[] mRotationMatrix) {
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int axisX = 0;
        int axisY = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                axisX = SensorManager.AXIS_X;// is this valid?
                axisY = SensorManager.AXIS_Y;// is this valid?
                break;

            case Surface.ROTATION_90:
                // examples says remapCoordinateSystem(inR, AXIS_Y, AXIS_MINUS_X, outR);
                axisX = SensorManager.AXIS_Y;
                axisY = SensorManager.AXIS_MINUS_X;
                break;

            case Surface.ROTATION_180:
                break;

            case Surface.ROTATION_270:
                break;

            default:
                Log.v("SurfaceRemap", "don't know the mScreenRotation value: "+ rotation + " you should never seen this message!");
                break;
        }
        boolean remapped = SensorManager.remapCoordinateSystem(mRotationMatrix, axisX, axisY, mRotationMatrix);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED && callbacks != null) {
            callbacks.onMagneticAccuracyChanged(accuracy);
        }
    }
}
