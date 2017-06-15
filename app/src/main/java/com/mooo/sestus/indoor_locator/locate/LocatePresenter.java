package com.mooo.sestus.indoor_locator.locate;

import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.SensorRepository;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LocatePresenter implements LocateContract.Presenter {
    private final SensorRepository sensorRepository;
    private final LocateContract.View view;
    private final FloorPlanRepository floorPlanRepository;
    private final Runnable locateRunnable;
    private final String floorPlanId;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture scheduledRecordingTask;
    private float[] averageMeasurement = new float[7];
    private int samplesCollected;
    private LinkedList<Integer> lastResolvedLocations = new LinkedList<>();
    private int lastResolvedPoint = -1;
    private HashMap<Integer, Integer> locationOccurences = new HashMap<>();


    public LocatePresenter(final FloorPlanRepository floorPlanRepository, final SensorRepository sensorRepository,
                           final LocateContract.View view, final String floorPlanId) {
        this.sensorRepository = sensorRepository;
        this.floorPlanRepository = floorPlanRepository;
        this.view = view;
        this.floorPlanId = floorPlanId;
        this.locateRunnable = new Runnable() {
            @Override
            public void run() {
                float[] measurements = LocatePresenter.this.sensorRepository.getMeasurement();
                averageMeasurement = ArrayUtils.addAll(measurements, averageMeasurement);
                if (++samplesCollected == 5) {
                    divideArrayBy(5, averageMeasurement);
                    int closestPoint = floorPlanRepository.getClosestPoint(measurements, floorPlanId);
                    float distance = floorPlanRepository.getLastComputedDistance();
                    if (distance >= 24) {
                        view.showDistance(String.format("Point: %d\n Dist: %.2f", closestPoint, distance));
                    } else {
                        addResolvedLocation(closestPoint);
                        int resolvedPoint = getResolvedPoint();
                        if (resolvedPoint != lastResolvedPoint) {
                            lastResolvedPoint = resolvedPoint;
                            view.showLocatedPointId(floorPlanRepository.getFloorPlanPoints(floorPlanId).get(lastResolvedPoint));
                            view.showDistance(String.format("Point: %d\n Dist: %.2f", closestPoint, distance));
                        }
                        else {
                            view.showDistance(String.format("Point: %d\n Dist: %.2f", closestPoint, distance));
                        }
                    }
                    samplesCollected = 0;
                    Arrays.fill(averageMeasurement, 0f);
                }

            }
        };
        view.setPresenter(this);
    }

    private void addResolvedLocation(int closestPoint) {
        if (lastResolvedLocations.size() >= 5) {
            lastResolvedLocations.removeFirst();
        }
        lastResolvedLocations.add(closestPoint);
    }

    private void divideArrayBy(int divider, float[] averageMeasurement) {
        for (int i = 0; i<averageMeasurement.length; i++)
            averageMeasurement[i] /= divider;
    }

    @Override
    public void start() {
        view.showFloorPlanImage(floorPlanRepository.getFloorPlanById(floorPlanId).getImage());
        if (scheduler == null || scheduler.isShutdown())
            scheduler = Executors.newScheduledThreadPool(1);
        sensorRepository.register(null);
        scheduledRecordingTask = scheduler.scheduleWithFixedDelay(locateRunnable, 400, 200, TimeUnit.MILLISECONDS);
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

    private int getResolvedPoint() {
        locationOccurences.clear();
        int point = -1;
        int maxFreq = Integer.MIN_VALUE;
        for (int pointId : lastResolvedLocations) {
            if (locationOccurences.get(pointId) == null)
                locationOccurences.put(pointId, 1);
            else {
                int freq = locationOccurences.get(pointId) + 1;
                locationOccurences.put(pointId, freq);
                if (freq > maxFreq) {
                    maxFreq = freq;
                    point = pointId;
                }
            }
        }
        return point;
    }
}
