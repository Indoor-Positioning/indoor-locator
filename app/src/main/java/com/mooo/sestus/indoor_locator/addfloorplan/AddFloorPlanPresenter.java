package com.mooo.sestus.indoor_locator.addfloorplan;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

/**
 * Created by mike on 5/27/17.
 */

public class AddFloorPlanPresenter implements AddFloorPlanContract.Presenter {

    private final FloorPlanRepository repository;
    private final AddFloorPlanContract.View view;
    private volatile boolean isStopped;

    public AddFloorPlanPresenter(FloorPlanRepository repository, AddFloorPlanContract.View view) {
        this.repository = repository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void start() {
        isStopped = false;
    }

    @Override
    public void stop() {
        isStopped = true;
    }

    @Override
    public void saveFloorPlan(String name, Bitmap photo) {
        if (Strings.isNullOrEmpty(name)) {
            view.showEmptyNameError();
            return;
        }
        if (photo == null) {
            view.showEmptyPhotoError();
            return;
        }
        if (repository.containsFloorPlan(name, photo)) {
            view.showFloorPlanNameAlreadyExists();
            return;
        }
        repository.addFloorPlan(name, photo, new FloorPlanRepository.SaveFloorPlanCallback() {
            @Override
            public void onFloorPlanSaved(FloorPlan floorPlan) {
                if (!isStopped)
                    viewFloorPlan(floorPlan);
            }

            @Override
            public void onError() {
                if (!isStopped)
                    view.showErrorOnSavingFloorPlan();
            }
        });

    }
    @Override
    public void addPhoto() {
        view.showPickPhotoDialog();
    }

    @Override
    public void viewFloorPlan(@NonNull FloorPlan addedFloorPlan) {
        view.startViewFloorPlanActivity(addedFloorPlan.getId());
    }
}
