package com.mooo.sestus.indoor_locator.addfloorplan;

import android.net.Uri;
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
        isStopped = false;
    }

    @Override
    public void saveFloorPlan(String name, Uri photo) {
        if (Strings.isNullOrEmpty(name))
            view.showEmptyNameError();
        if (photo == null)
            view.showEmptyPhotoError();
    }

    @Override
    public void viewFloorPlan(@NonNull FloorPlan addedFloorPlan) {

    }
}
