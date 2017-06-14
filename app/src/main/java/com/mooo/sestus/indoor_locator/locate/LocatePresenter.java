package com.mooo.sestus.indoor_locator.locate;

import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.SensorRepository;
import com.mooo.sestus.indoor_locator.scan.MagneticScanPresenter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LocatePresenter implements LocateContract.Presenter, SensorRepository.Callback {
    private final SensorRepository sensorRepository;
    private final LocateContract.View view;
    private final FloorPlanRepository floorPlanRepository;
    private final Runnable locateRunnalbe;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture scheduledRecordingTask;


    public LocatePresenter(final FloorPlanRepository floorPlanRepository, final SensorRepository sensorRepository,
                           final LocateContract.View view, final String floorPlanId) {
        this.sensorRepository = sensorRepository;
        this.floorPlanRepository = floorPlanRepository;
        this.view = view;
        this.locateRunnalbe = new Runnable() {
            @Override
            public void run() {
                float[] measurements = LocatePresenter.this.sensorRepository.getMeasurement();
                int closest = floorPlanRepository.getClosestPoint(measurements, floorPlanId);
                if (closest == -1) {
                    //no fingerprints for this floor plan
                }
                else
                    view.showLocatedPointId(closest);

            }
        };
        view.setPresenter(this);
    }

    @Override
    public void start() {
        if (scheduler == null || scheduler.isShutdown())
            scheduler = Executors.newScheduledThreadPool(1);
        sensorRepository.register(this);
        scheduledRecordingTask = scheduler.scheduleWithFixedDelay(locateRunnalbe, 400, 950, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        sensorRepository.deRegister();
        scheduler.shutdown();
    }

    @Override
    public void startRecording() {
    }

    @Override
    public void stopRecording() {
        scheduledRecordingTask.cancel(true);
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
    public void onRotationSensorChanged(float azimuth, float pitch, float roll) {
        view.updateAzimuth(azimuth);
        view.updatePitch(pitch);
        view.updateRoll(roll);
    }

}
