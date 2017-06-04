package com.mooo.sestus.indoor_locator.selectfloorplan;

import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

class SelectFloorPlanPresenter implements SelectFloorPlanContract.Presenter {
    private final FloorPlanRepository floorPlanRepository;
    private final SelectFloorPlanContract.View view;
    private volatile boolean isStopped;

    SelectFloorPlanPresenter(FloorPlanRepository floorPlanRepository, SelectFloorPlanContract.View view) {
        this.floorPlanRepository = checkNotNull(floorPlanRepository);
        this.view = checkNotNull(view);
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        isStopped = false;
        loadFloorPlans();
    }

    @Override
    public void stop() {
        isStopped = true;
    }

    @Override
    public void addNewFloorPlan() {
        view.startAddFloorPlanActivity();
    }

    @Override
    public void loadFloorPlans() {

        floorPlanRepository.getFloorPlans(new FloorPlanRepository.LoadFloorPlansCallback() {
            @Override
            public void onFloorPlansLoaded(SortedSet<FloorPlan> floorPlans) {
                if (!isStopped)
                    view.updateFloorPlanList(floorPlans);
            }

            @Override
            public void onDataNotAvailable() {
                if (!isStopped)
                    view.showErrorOnLoadingFloorPlans();
            }
        });
    }

    @Override
    public void viewFloorPlan(@NonNull FloorPlan requestedFloorPlan) {
        view.startViewFloorPlanActivity(requestedFloorPlan.getId());
    }
}
