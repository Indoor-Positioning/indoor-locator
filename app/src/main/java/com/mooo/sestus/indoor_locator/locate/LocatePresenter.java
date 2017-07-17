package com.mooo.sestus.indoor_locator.locate;

import android.graphics.PointF;
import android.util.Log;

import com.mooo.sestus.indoor_locator.data.FingerPrint;
import com.mooo.sestus.indoor_locator.data.FingerPrintedLocation;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.PointOfInterest;
import com.mooo.sestus.indoor_locator.data.ResolvedLocation;
import com.mooo.sestus.indoor_locator.data.SensorRepository;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LocatePresenter implements LocateContract.Presenter {
    private final SensorRepository sensorRepository;
    private final LocateContract.View view;
    private final FloorPlanRepository floorPlanRepository;
    private final Runnable locateRunnable;
    private final int floorPlanId;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture scheduledRecordingTask;
    private FingerPrint averagedFingerprint = new FingerPrint();
    private int samplesCollected;
    private LinkedList<Integer> lastResolvedLocations = new LinkedList<>();
    private int lastResolvedPoint = -1;
    private HashMap<Integer, Integer> locationOccurences = new HashMap<>();
    private static final double ORIENTATION_DISTANCE_NORMALIZATION_FACTOR = 0.2;
    private static final int WIFI_RSSI_DISTANCE_NORMALIZATION_FACTOR = 4;
    private Map<Integer, List<Float[]>> fingerPrints;


    public LocatePresenter(final FloorPlanRepository floorPlanRepository, final SensorRepository sensorRepository,
                           final LocateContract.View view, int floorPlanId) {
        this.sensorRepository = sensorRepository;
        this.floorPlanRepository = floorPlanRepository;
        this.view = view;
        this.floorPlanId = floorPlanId;
        this.locateRunnable = createLocateTask();
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
        final List<PointF> pois = new ArrayList<>();
        floorPlanRepository.getFloorPlanPois(floorPlanId, new FloorPlanRepository.LoadPointOfInterestsCallback() {
            @Override
            public void onPoisLoaded(Collection<PointOfInterest> locations) {
                for (PointOfInterest location: locations) {
                    pois.add(new PointF(location.getXCoord(), location.getYCoord()));
                }
                view.showFloorPlanImage(floorPlanRepository.getFloorPlanById(floorPlanId).getResourceName(), pois);
            }

            @Override
            public void onDataNotAvailable() {}
        });
        if (scheduler == null || scheduler.isShutdown())
            scheduler = Executors.newScheduledThreadPool(1);
        sensorRepository.register(null);
        scheduledRecordingTask = scheduler.scheduleWithFixedDelay(locateRunnable, 400, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        scheduledRecordingTask.cancel(true);
        sensorRepository.deRegister();
        scheduler.shutdown();
    }

    @Override
    public void startRecording() {}

    @Override
    public void stopRecording() {}

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

    private int getClosestPoint(FingerPrint fingerPrint) {
//        if (fingerPrints == null)
//            return -1;
//        Set<Map.Entry<Integer, List<Float[]>>> entrySet = fingerPrints.entrySet();
//        int distance = Integer.MAX_VALUE;
//        int pointId = 0;
//        Log.v("LOCATE", String.format("Locating fingerprint %s on %d points", Arrays.toString(measurement), entrySet.size()));
//        for (Map.Entry<Integer, List<Float[]>> entry : entrySet) {
//            Log.v("LOCATE", String.format("Trying Point %d which has %d fingerprints", entry.getKey(), entry.getValue().size()));
//            for (Float[] fingerprint: entry.getValue()) {
//                int newDistance = computeDistance(fingerprint, measurement);
//                if (newDistance < distance) {
//                    distance = newDistance;
//                    pointId = entry.getKey();
//                }
//            }
//        }
//        lastComputedDistance = distance;
//        return pointId;
        return 0;
    }


    private int computeDistance(Float[] fingerprint, float[] measurement) {
        float distance = 0;

        distance += magneticFieldDistance(fingerprint, measurement);

        distance += orientationDistance(fingerprint, measurement);

        distance += wifiRssiDistance(fingerprint, measurement);

        return (int) distance;
    }

    private float wifiRssiDistance(Float[] fingerprint, float[] measurement) {
        float distance = 0 ;
        if (!fingerprint[6].equals(0f) && measurement[6] != 0)
            distance += WIFI_RSSI_DISTANCE_NORMALIZATION_FACTOR * Math.abs(fingerprint[6] - measurement[6]);
        return distance;
    }

    private float orientationDistance(Float[] fingerprint, float[] measurement) {
        float distance = 0;
        for (int i = 3; i < 6; i++) {
            distance += ORIENTATION_DISTANCE_NORMALIZATION_FACTOR * Math.abs(fingerprint[i] - measurement[i]);
        }
        return distance;
    }

    private float magneticFieldDistance(Float[] fingerprint, float[] measurement) {
        float distance = 0;
        for (int i = 0; i < 3; i++) {
            distance += Math.abs(fingerprint[i] - measurement[i]);
        }
        return distance;
    }

    private Runnable createLocateTask() {
        return new Runnable() {
            @Override
            public void run() {
                FingerPrint fingerPrint = LocatePresenter.this.sensorRepository.getCurrentFingerPrint();
                averagedFingerprint.add(fingerPrint);
                if (++samplesCollected == 5) {
                    averagedFingerprint.divideBy(5);
                    floorPlanRepository.getClosestPoint(floorPlanId, averagedFingerprint, new FloorPlanRepository.LocateCallback() {
                        @Override
                        public void onLocateResult(ResolvedLocation resolvedLocation) {
                            if (resolvedLocation.getFingerPrintDistance() >= 24) {
                                view.showDistance(String.format(Locale.getDefault(), "Point: %d\n Dist: %d",
                                        resolvedLocation.getClosestFingerPrintedLocation(), resolvedLocation.getFingerPrintDistance()));
                            } else {
                                addResolvedLocation(resolvedLocation.getClosestFingerPrintedLocation());
                                int resolvedPoint = getResolvedPoint();
                                if (resolvedPoint != lastResolvedPoint) {
                                    lastResolvedPoint = resolvedPoint;
                                    FingerPrintedLocation point = floorPlanRepository.getFingerPrintedLocationById(lastResolvedPoint);
                                    if (point.isPoi()) {
                                        view.showIsOnPoi(new PointF(point.getXCoord(), point.getYCoord()), floorPlanRepository.getPointOfInterestById(point.getRelatedPoi()).getName());
                                    }
                                    else {
                                        view.showLocatedPointId(new PointF(point.getXCoord(), point.getYCoord()));
                                        PointOfInterest nearByPoi = floorPlanRepository.getPointOfInterestById(resolvedLocation.getClosestPoi());
                                        if (nearByPoi != null)
                                            view.showNearestPoi(new PointF(nearByPoi.getXCoord(), nearByPoi.getYCoord()));
                                    }

                                    view.showDistance(String.format(Locale.getDefault(), "Point: %d\n Dist: %d",
                                            resolvedLocation.getClosestFingerPrintedLocation(), resolvedLocation.getFingerPrintDistance()));
                                }
                                else {
                                    view.showDistance(String.format(Locale.getDefault(), "Point: %d\n Dist: %d",
                                            resolvedLocation.getClosestFingerPrintedLocation(), resolvedLocation.getFingerPrintDistance()));
                                }
                            }
                            samplesCollected = 0;
                            averagedFingerprint.clear();
                        }
                        @Override
                        public void onError() {}
                    });

                }
            }
        };
    }


}
