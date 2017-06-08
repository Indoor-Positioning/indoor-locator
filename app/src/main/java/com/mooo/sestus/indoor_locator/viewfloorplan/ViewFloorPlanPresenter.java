package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ViewFloorPlanPresenter implements ViewFloorPlanContract.Presenter {

    private final FloorPlanRepository repository;
    private final ViewFloorPlanContract.View view;
    private volatile boolean isStopped;
    private FloorPlan floorPlan;
    private PointF awaitingPin;
    private List<PointF> floorPlanPoints;
    private PointF selectedExistingPoint;

    public ViewFloorPlanPresenter(FloorPlanRepository floorPlanRepository, ViewFloorPlanContract.View view, String floorPlanId) {
        this.repository = floorPlanRepository;
        floorPlan = repository.getFloorPlanById(floorPlanId);
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        isStopped = false;
        floorPlanPoints = new ArrayList<>(repository.getFloorPlanPoints(floorPlan.getId()));
        view.showFloorPlanImage(floorPlan.getImage(), floorPlanPoints);
    }

    @Override
    public void stop() {
        isStopped = true;
    }


    @Override
    public void onPinConfirmed() {
        repository.addPointToFloorPlan(floorPlan.getId(), awaitingPin);
        floorPlanPoints.add(awaitingPin);
        view.showPin(awaitingPin);
        awaitingPin = null;
    }

    @Override
    public void recordFingerprints(PointF pin) {
        int pinId = repository.getPinId(floorPlan.getId(), pin);
        view.showStartScanningActivity(floorPlan.getId(), pinId);
    }

    @Override
    public void onUserClickedFloorPlan(PointF point) {
        if (awaitingPin != null)
            view.removePin(awaitingPin);
        if (selectedExistingPoint != null)
            view.showPin(selectedExistingPoint);
        PointF nearestPoint = findClosestPoint(point);
        if (nearestPoint != null && areClose(point, nearestPoint)) {
            selectedExistingPoint = nearestPoint;
            view.showSelectedPin(nearestPoint);
        }
        else {
            awaitingPin = point;
            view.showConfirmAddPinToFloorPlan(awaitingPin);
        }
    }

    private boolean areClose(PointF point, PointF nearestPoint) {
        float xDist = Math.abs(point.x - nearestPoint.x);
        float yDist = Math.abs(point.y - nearestPoint.y);
        return xDist < 120 && yDist < 120;
    }

    private PointF findClosestPoint(PointF requestedPoint) {
        float finalSquareDist = Float.MAX_VALUE;
        PointF closestPoint = null;
        for (PointF point: floorPlanPoints) {
            float xDist = Math.abs(point.x * point.x - requestedPoint.x * requestedPoint.x);
            float yDist = Math.abs(point.y * point.y - requestedPoint.y * requestedPoint.y);
            float squareDist = xDist + yDist;
            if ((xDist + yDist) < finalSquareDist) {
                finalSquareDist = squareDist;
                closestPoint = point;
            }
        }
        return closestPoint;
    }
}
