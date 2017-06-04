package com.mooo.sestus.indoor_locator.data;

import android.graphics.Bitmap;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;

import java.util.SortedSet;

/**
 * Created by mike on 5/29/17.
 */

public interface FloorPlanRepository {

    void saveFloorPlan(String name, Bitmap bitmap, SaveFloorPlanCallback callback);

    boolean containsFloorPlan(String name, Bitmap bitmap);

    interface SaveFloorPlanCallback {
        void onFloorPlanSaved(FloorPlan floorPlan);

        void onError();
    }

    interface LoadFloorPlansCallback {
        void onFloorPlansLoaded(SortedSet<FloorPlan> floorPlans);

        void onDataNotAvailable();
    }

    void getFloorPlans(@NonNull LoadFloorPlansCallback callback);
}
