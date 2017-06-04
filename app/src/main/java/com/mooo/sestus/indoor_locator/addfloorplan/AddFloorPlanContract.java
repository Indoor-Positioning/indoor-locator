package com.mooo.sestus.indoor_locator.addfloorplan;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;
import com.mooo.sestus.indoor_locator.data.FloorPlan;


public interface AddFloorPlanContract {

    interface View extends BaseView<Presenter> {
        void showPickPhotoDialog();

        void showEmptyNameError();

        void showEmptyPhotoError();

        void startViewFloorPlanActivity(String floorPlanId);

    }

    interface Presenter extends BasePresenter {
        void saveFloorPlan(String name, Uri photo);

        void viewFloorPlan(@NonNull FloorPlan addedFloorPlan);


    }
}
