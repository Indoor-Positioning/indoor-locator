package com.mooo.sestus.indoor_locator.scan;

import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.SensorRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MagneticScanPresenter implements MagneticScanContract.Presenter, SensorRepository.Callback {
    private final SensorRepository sensorRepository;
    private final MagneticScanContract.View view;
    private final Runnable recordingRunnable;
    private final FloorPlanRepository floorPlanRepository;
    private final int pointId;
    private final String floorPlanId;
    private ScheduledFuture scheduledRecordingTask;
    private ScheduledExecutorService scheduler;
    private int measurementCount = 0;
    private float[] newMeasurement = new float[7];
    private int fingerPrintsAdded = 0;

    public MagneticScanPresenter(final FloorPlanRepository floorPlanRepository, final SensorRepository sensorRepository,
                                 MagneticScanContract.View view, final String floorPlanId, final int pointId) {
        this.sensorRepository = sensorRepository;
        this.floorPlanRepository = floorPlanRepository;
        this.view = view;
        this.floorPlanId = floorPlanId;
        this.pointId = pointId;
        this.recordingRunnable = new Runnable() {
            @Override
            public void run() {
                float[] measurements = MagneticScanPresenter.this.sensorRepository.getMeasurement();
                processMeasurement(measurements);
            }
        };
        view.setPresenter(this);
    }

    @Override
    public void start() {
        if (scheduler == null || scheduler.isShutdown())
            scheduler = Executors.newScheduledThreadPool(1);
        sensorRepository.register(this);
    }

    @Override
    public void stop() {
        sensorRepository.deRegister();
        scheduler.shutdown();
    }

    @Override
    public void startRecording() {
        fingerPrintsAdded = 0;
        scheduledRecordingTask = scheduler.scheduleWithFixedDelay(recordingRunnable, 500, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stopRecording() {
        scheduledRecordingTask.cancel(true);
        floorPlanRepository.saveFingerPrints(floorPlanId, pointId);
        view.showAddedFingerPrints(fingerPrintsAdded);
    }

    @Override
    public void onMagneticAccuracyChanged(int accuracy) {
        view.updateMagneticAccuracy(accuracy);
    }

    @Override
    public void onRotationAccuracyChanged(int accuracy) {
        view.updateRotationVectorAccuracy(accuracy);
    }

    @Override
    public void onMagneticSensorChanged(float x, float y, float z) {
        view.updateMagneticFieldX(x);
        view.updateMagneticFieldY(y);
        view.updateMagneticFieldZ(z);
    }

    @Override
    public void onRotationSensorChanged(float azimuth, float pitch, float roll, float wifiRssi) {
        view.updateAzimuth(azimuth);
        view.updatePitch(pitch);
        view.updateRoll(roll);
        view.updateRssi(wifiRssi);
    }

    private void processMeasurement(float[] measurement) {
        measurementCount++;
        for (int i = 0; i < measurement.length; i++) {
            newMeasurement[i] += measurement[i];
            if (measurementCount == 5)
                newMeasurement[i] /= 5;
        }
        if (measurementCount == 5) {
            floorPlanRepository.addFingerPrint(floorPlanId, pointId, newMeasurement);
            fingerPrintsAdded++;
            measurementCount = 0;
        }
    }
}
