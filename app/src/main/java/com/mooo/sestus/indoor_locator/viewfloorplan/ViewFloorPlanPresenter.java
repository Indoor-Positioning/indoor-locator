package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import java.util.Collections;


public class ViewFloorPlanPresenter implements ViewFloorPlanContract.Presenter {

    private final FloorPlanRepository repository;
    private final ViewFloorPlanContract.View view;
    private volatile boolean isStopped;
    private FloorPlan floorPlan;
    private PointF awaitingPin;

    public ViewFloorPlanPresenter(FloorPlanRepository floorPlanRepository, ViewFloorPlanContract.View view, String floorPlanId) {
        this.repository = floorPlanRepository;
        floorPlan = repository.getFloorPlanById(floorPlanId);
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        isStopped = false;
        view.showFloorPlanImage(floorPlan.getImage(), repository.getFloorPlanPoints(floorPlan.getId()));
    }

    @Override
    public void stop() {
        isStopped = true;
    }


    @Override
    public void onPinConfirmed() {
        repository.addPointToFloorPlan(floorPlan.getId(), awaitingPin);
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
        awaitingPin = point;
        view.showConfirmAddPinToFloorPlan(awaitingPin);
    }
}
