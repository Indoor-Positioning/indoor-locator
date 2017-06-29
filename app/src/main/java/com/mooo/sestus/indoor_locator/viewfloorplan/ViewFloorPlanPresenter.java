package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.data.FingerPrintedLocation;
import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.PointOfInterest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ViewFloorPlanPresenter implements ViewFloorPlanContract.Presenter {

    private static final float CLOSE_DISTANCE_THRESHOLD = 120;
    private final FloorPlanRepository repository;
    private final ViewFloorPlanContract.View view;
    private final int floorPlanId;
    private volatile boolean isStopped;
    private FloorPlan floorPlan;
    private FingerPrintedLocation awaitingPin;
    private PointOfInterest awaitingPoi;
    private PointOfInterest selectedExistingPoi;
    private FingerPrintedLocation selectedExistingPoint;
    private List<FingerPrintedLocation> floorPlanPoints;
    private List<PointOfInterest> floorPlanPois;


    public ViewFloorPlanPresenter(FloorPlanRepository floorPlanRepository, ViewFloorPlanContract.View view, int floorPlanId) {
        this.repository = floorPlanRepository;
        floorPlan = repository.getFloorPlanById(floorPlanId);
        this.view = view;
        this.floorPlanId = floorPlanId;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        isStopped = false;
        repository.getFingerPrintedLocations(floorPlanId, new FloorPlanRepository.LoadFingerPrintedLocationsCallback() {
            @Override
            public void onLocationsLoaded(Collection<FingerPrintedLocation> locations) {
                floorPlanPoints = new ArrayList<>(locations);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
        repository.getFloorPlanPois(floorPlanId, new FloorPlanRepository.LoadPointOfInterestsCallback() {
            @Override
            public void onPoisLoaded(Collection<PointOfInterest> points) {
                floorPlanPois = new ArrayList<>(points);
                List<PointF> locations = new ArrayList<>();
                List<PointF> pois = new ArrayList<>();
                for (FingerPrintedLocation loc : floorPlanPoints) {
                    if (!loc.isPoi())
                        locations.add(new PointF(loc.getXCoord(), loc.getYCoord()));
                }
                for (PointOfInterest poi: floorPlanPois)
                    pois.add(new PointF(poi.getXCoord(), poi.getYCoord()));
                view.showFloorPlanImage(floorPlan.getResourceName(), locations, pois);
            }
            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void stop() {
        isStopped = true;
    }

    @Override
    public void onPointConfirmed() {
        if (awaitingPoi != null)
            onPoiConfirmed();
        else
            onFingerPrintedLocConfirmed();
    }

    private void onPoiConfirmed() {
        PointF pointF = new PointF(awaitingPoi.getXCoord(), awaitingPoi.getYCoord());
        view.showPoi(pointF);
        repository.addPoiToFloorPlan(floorPlan.getId(), pointF, new FloorPlanRepository.AddPoiCallback() {
            @Override
            public void onPoiAdded(PointOfInterest poi) {
                floorPlanPois.add(poi);
            }

            @Override
            public void onError() {

            }
        });
        awaitingPoi = null;
    }

    private void onFingerPrintedLocConfirmed() {
        PointF pointF = new PointF(awaitingPin.getXCoord(), awaitingPin.getYCoord());
        view.showPin(pointF);
        repository.addFingerPrintedLocation(floorPlan.getId(), pointF, new FloorPlanRepository.AddFingerPrintedLocationCallback() {
            @Override
            public void onLocationAdded(FingerPrintedLocation location) {
                floorPlanPoints.add(location);
            }

            @Override
            public void onError() {

            }
        });
        awaitingPin = null;
    }

    @Override
    public void onUserClickedFloorPlan(PointF pointF) {
        FingerPrintedLocation point = new FingerPrintedLocation(floorPlanId, pointF.x, pointF.y);
        if (awaitingPin != null)
            view.removePin(new PointF(awaitingPin.getXCoord(), awaitingPin.getYCoord()));
        if (awaitingPoi != null)
            view.removePoi(new PointF(awaitingPoi.getXCoord(), awaitingPoi.getYCoord()));
        if (selectedExistingPoint != null)
            view.showPin(new PointF(selectedExistingPoint.getXCoord(), selectedExistingPoint.getYCoord()));
        if (selectedExistingPoi != null)
            view.showPoi(new PointF(selectedExistingPoi.getXCoord(), selectedExistingPoi.getYCoord()));
        FingerPrintedLocation nearestPoint = findClosestPoint(point);
        if (nearestPoint != null && areClose(point, nearestPoint)) {
            selectedExistingPoint = nearestPoint;
            view.showSelectedPin(new PointF(nearestPoint.getXCoord(), nearestPoint.getYCoord()));
        }
        else {
            awaitingPin = point;
            view.showConfirmAddPinToFloorPlan(new PointF(awaitingPin.getXCoord(), awaitingPin.getYCoord()));
        }
    }

    @Override
    public void onUserLongClickedFloorPlan(PointF pointF) {
        PointOfInterest point = new PointOfInterest(floorPlanId, pointF.x, pointF.y);
        if (awaitingPin != null)
            view.removePin(new PointF(awaitingPin.getXCoord(), awaitingPin.getYCoord()));
        if (awaitingPoi != null)
            view.removePoi(new PointF(awaitingPoi.getXCoord(), awaitingPoi.getYCoord()));
        if (selectedExistingPoint != null)
            view.showPin(new PointF(selectedExistingPoint.getXCoord(), selectedExistingPoint.getYCoord()));
        if (selectedExistingPoi != null)
            view.showPoi(new PointF(selectedExistingPoi.getXCoord(), selectedExistingPoi.getYCoord()));
        PointOfInterest nearestPoi = findClosestPoi(point);
        if (nearestPoi != null && areClose(point, nearestPoi)) {
            selectedExistingPoi = nearestPoi;
            view.showSelectedPoi(new PointF(nearestPoi.getXCoord(), nearestPoi.getYCoord()));
        }
        else {
            awaitingPoi = point;
            view.showConfirmAddPoiToFloorPlan(new PointF(awaitingPoi.getXCoord(), awaitingPoi.getYCoord()));
        }
    }

    @Override
    public void scanSelectedPoint() {
        if (selectedExistingPoint != null) {
            view.showStartScanningActivity(selectedExistingPoint.getId());
            selectedExistingPoint = null;
        }
        else if (selectedExistingPoi != null) {
            view.showStartScanningActivity(selectedExistingPoi.getRelatedFingerPrintedLocId());
            selectedExistingPoi= null;
        }
    }

    private boolean areClose(FingerPrintedLocation point, FingerPrintedLocation nearestPoint) {
        float xDist = Math.abs(point.getXCoord() - nearestPoint.getXCoord());
        float yDist = Math.abs(point.getYCoord() - nearestPoint.getYCoord());
        return xDist < CLOSE_DISTANCE_THRESHOLD && yDist < CLOSE_DISTANCE_THRESHOLD;
    }

    private boolean areClose(PointOfInterest point, PointOfInterest nearestPoint) {
        float xDist = Math.abs(point.getXCoord() - nearestPoint.getXCoord());
        float yDist = Math.abs(point.getYCoord() - nearestPoint.getYCoord());
        return xDist < CLOSE_DISTANCE_THRESHOLD && yDist < CLOSE_DISTANCE_THRESHOLD;
    }

    private FingerPrintedLocation findClosestPoint(FingerPrintedLocation requestedPoint) {
        float finalSquareDist = Float.MAX_VALUE;
        FingerPrintedLocation closestPoint = null;
        for (FingerPrintedLocation point: floorPlanPoints) {
            float xDist = Math.abs(point.getXCoord() * point.getXCoord() - requestedPoint.getXCoord() * requestedPoint.getXCoord());
            float yDist = Math.abs(point.getYCoord() * point.getYCoord() - requestedPoint.getYCoord() * requestedPoint.getYCoord());
            float squareDist = xDist + yDist;
            if ((xDist + yDist) < finalSquareDist) {
                finalSquareDist = squareDist;
                closestPoint = point;
            }
        }
        return closestPoint;
    }


    private PointOfInterest findClosestPoi(PointOfInterest requestedPoint) {
        float finalSquareDist = Float.MAX_VALUE;
        PointOfInterest closestPoint = null;
        for (PointOfInterest point: floorPlanPois) {
            float xDist = Math.abs(point.getXCoord() * point.getXCoord() - requestedPoint.getXCoord() * requestedPoint.getXCoord());
            float yDist = Math.abs(point.getYCoord() * point.getYCoord() - requestedPoint.getYCoord() * requestedPoint.getYCoord());
            float squareDist = xDist + yDist;
            if ((xDist + yDist) < finalSquareDist) {
                finalSquareDist = squareDist;
                closestPoint = point;
            }
        }
        return closestPoint;
    }

}
