package com.mooo.sestus.indoor_locator.selectfloorplan;

import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;
import com.mooo.sestus.indoor_locator.data.FloorPlan;

import java.util.List;

/**
 * Created by mike on 5/27/17.
 */

public interface SelectFloorPlanContract {

    interface view extends BaseView<Presenter> {
        void setLoadingIndicator();

        void updateFloorPlanList(List<FloorPlan> floorPlanList);

        void startViewFloorPlanActivity();

        void startAddFloorPlanActivity();

    }

    interface Presenter extends BasePresenter {
        void addNewFloorPlan();

        void viewFloorPlan(@NonNull FloorPlan requestedFloorPlan);

        void loadFloorPlans();
    }
}
