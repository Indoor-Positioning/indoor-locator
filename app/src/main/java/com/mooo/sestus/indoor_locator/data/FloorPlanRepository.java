package com.mooo.sestus.indoor_locator.data;

import android.support.annotation.NonNull;

import java.util.SortedSet;

/**
 * Created by mike on 5/29/17.
 */

public interface FloorPlanRepository {

    interface LoadFloorPlansCallback {
        void onFloorPlansLoaded(SortedSet<FloorPlan> floorPlans);

        void onDataNotAvailable();
    }

    SortedSet<FloorPlan> getFloorPlans(@NonNull LoadFloorPlansCallback callback);
}
