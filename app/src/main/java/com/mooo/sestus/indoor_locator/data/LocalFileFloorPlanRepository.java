package com.mooo.sestus.indoor_locator.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

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
    public void getFloorPlans(@NonNull FloorPlanRepository.LoadFloorPlansCallback callback) {
        return;
    }
}
