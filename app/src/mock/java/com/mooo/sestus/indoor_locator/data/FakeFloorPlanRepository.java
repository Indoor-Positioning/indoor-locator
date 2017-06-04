package com.mooo.sestus.indoor_locator.data;

import android.graphics.Bitmap;
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
        floorPlans.add(new FloorPlan("FP_1", Bitmap.createBitmap(42, 42, Bitmap.Config.ARGB_8888)));
        floorPlans.add(new FloorPlan("FP_2", Bitmap.createBitmap(42, 42, Bitmap.Config.ARGB_8888)));
        floorPlans.add(new FloorPlan("FP_3", Bitmap.createBitmap(42, 42, Bitmap.Config.ARGB_8888)));
    }

    public SortedSet<FloorPlan> getFloorPlans() {
        return Collections.unmodifiableSortedSet(floorPlans);
    }


    @Override
    public void saveFloorPlan(String asd, Bitmap bitmap, SaveFloorPlanCallback callback) {
        FloorPlan newFloorPlan = new FloorPlan(asd, bitmap);
        floorPlans.add(newFloorPlan);
        callback.onFloorPlanSaved(newFloorPlan);
    }

    @Override
    public boolean containsFloorPlan(String name, Bitmap bitmap) {
        return floorPlans.contains(new FloorPlan(name, bitmap));
    }

    @Override
    public void getFloorPlans(@NonNull LoadFloorPlansCallback callback) {
        callback.onFloorPlansLoaded(Collections.unmodifiableSortedSet(floorPlans));
    }
}
