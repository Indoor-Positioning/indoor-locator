package com.mooo.sestus.indoor_locator.selectfloorplan;

import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;
import com.mooo.sestus.indoor_locator.data.FloorPlan;

import java.util.List;
import java.util.SortedSet;

/**
 * Created by mike on 5/27/17.
 */

interface SelectFloorPlanContract {

    interface view extends BaseView<Presenter> {
        void setLoadingIndicator();

        void showErrorOnLoadingFloorPlans();

        void updateFloorPlanList(SortedSet<FloorPlan> floorPlanList);

        void startViewFloorPlanActivity(String floorPlanId);

        void startAddFloorPlanActivity();

    }

    interface Presenter extends BasePresenter {
        void addNewFloorPlan();

        void viewFloorPlan(@NonNull FloorPlan requestedFloorPlan);

        void loadFloorPlans();
    }
}
