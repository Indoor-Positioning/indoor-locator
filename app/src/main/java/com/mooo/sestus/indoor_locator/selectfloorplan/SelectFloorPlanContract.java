package com.mooo.sestus.indoor_locator.selectfloorplan;

import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;
import com.mooo.sestus.indoor_locator.data.FloorPlan;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;


interface SelectFloorPlanContract {

    interface View extends BaseView<Presenter> {

        void showErrorOnLoadingFloorPlans();

        void updateFloorPlanList(Collection<FloorPlan> floorPlanList);

        void startViewFloorPlanActivity(int floorPlanId);

        void startAddFloorPlanActivity();

    }

    interface Presenter extends BasePresenter {
        void addNewFloorPlan();

        void viewFloorPlan(@NonNull FloorPlan requestedFloorPlan);

        void loadFloorPlans();
    }
}
