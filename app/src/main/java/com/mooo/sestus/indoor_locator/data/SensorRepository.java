package com.mooo.sestus.indoor_locator.data;

public interface SensorRepository {

    void register(Callback callback);

    void deRegister();

    FingerPrint getCurrentFingerPrint();


    interface Callback {
        void onMagneticAccuracyChanged(int accuracy);

        void onMagneticSensorChanged(float x, float y, float z);

        //TODO: This is hacky, remove wifiRssi from onRotationSensorChanged Callback - define a new one
        void onRotationSensorChanged(float azimuth, float pitch, float roll, float wifiRssi);
    }
}
