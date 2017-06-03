package com.mooo.sestus.indoor_locator.data;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class FakeFloorPlanRepository implements FloorPlanRepository {

    private static FakeFloorPlanRepository INSTANCE;
    private final TreeSet<FloorPlan> floorPlans;


    public static FakeFloorPlanRepository getInstance() {
    // Not Thread safe (it doesn't need to be)
        if (INSTANCE == null)
            INSTANCE = new FakeFloorPlanRepository();
        return INSTANCE;
    }

    private FakeFloorPlanRepository() {
        floorPlans = new TreeSet<>();
        addFakeFloorPlans();
    }

    private void addFakeFloorPlans() {
        floorPlans.add(new FloorPlan("FP_1"));
        floorPlans.add(new FloorPlan("FP_1"));
        floorPlans.add(new FloorPlan("FP_3"));
    }

    public SortedSet<FloorPlan> getFloorPlans() {
        return Collections.unmodifiableSortedSet(floorPlans);
    }


    @Override
    public void getFloorPlans(@NonNull LoadFloorPlansCallback callback) {
        callback.onFloorPlansLoaded(Collections.unmodifiableSortedSet(floorPlans));
    }
}
