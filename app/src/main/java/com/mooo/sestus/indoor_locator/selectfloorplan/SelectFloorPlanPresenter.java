package com.mooo.sestus.indoor_locator.selectfloorplan;

import android.support.annotation.NonNull;
import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

class SelectFloorPlanPresenter implements SelectFloorPlanContract.Presenter {
    private final FloorPlanRepository floorPlanRepository;
    private final SelectFloorPlanContract.view view;

    SelectFloorPlanPresenter(FloorPlanRepository floorPlanRepository, SelectFloorPlanContract.view view) {
        this.floorPlanRepository = checkNotNull(floorPlanRepository);
        this.view = checkNotNull(view);
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        floorPlanRepository.getFloorPlans(new FloorPlanRepository.LoadFloorPlansCallback() {
            @Override
            public void onFloorPlansLoaded(SortedSet<FloorPlan> floorPlans) {
                view.updateFloorPlanList(floorPlans);
            }

            @Override
            public void onDataNotAvailable() {
                view.showErrorOnLoadingFloorPlans();
            }
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public void addNewFloorPlan() {

    }

    @Override
    public void loadFloorPlans() {

    }

    @Override
    public void viewFloorPlan(@NonNull FloorPlan requestedFloorPlan) {

    }
}
