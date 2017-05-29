package com.mooo.sestus.indoor_locator.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mike on 5/29/17.
 */

public class LocalFileFloorPlanRepository implements FloorPlanRepository {
    private static LocalFileFloorPlanRepository INSTANCE;

    private LocalFileFloorPlanRepository(@NonNull Context context) {

    }

    public static LocalFileFloorPlanRepository getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new LocalFileFloorPlanRepository(context);
        return INSTANCE;
    }

    @Override
    public SortedSet<FloorPlan> getFloorPlans(@NonNull FloorPlanRepository.LoadFloorPlansCallback callback) {
        return null;
    }
}
