package com.mooo.sestus.indoor_locator.addfloorplan;

import android.graphics.Bitmap;
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

        void startViewFloorPlanActivity(int floorPlanId);

        void showErrorOnSavingFloorPlan();

        void showFloorPlanNameAlreadyExists();
    }

    interface Presenter extends BasePresenter {
        void saveFloorPlan(String name, Bitmap photo);

        void addPhoto();

        void viewFloorPlan(@NonNull FloorPlan addedFloorPlan);


    }
}
