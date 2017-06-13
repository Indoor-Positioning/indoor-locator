package com.mooo.sestus.indoor_locator.data;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by mike on 5/29/17.
 */

public interface FloorPlanRepository {

    void addPointToFloorPlan(String floorPlanId, PointF pointF);

    List<PointF> getFloorPlanPoints(String floorPlanId);

    int getPinId(String floorplanId, PointF point);

    void addFloorPlan(String name, Bitmap bitmap, SaveFloorPlanCallback callback);

    boolean containsFloorPlan(String name, Bitmap bitmap);

    FloorPlan getFloorPlanById(String floorPlanId);

    void addFingerPrint(String floorPlanId, int pointId, float[] measurements);

    interface SaveFloorPlanCallback {
        void onFloorPlanSaved(FloorPlan floorPlan);

        void onError();
    }

    interface LoadFloorPlansCallback {
        void onFloorPlansLoaded(Collection<FloorPlan> floorPlans);

        void onDataNotAvailable();
    }


    int getClosestPoint(float[] fingerprint, String floorPlanId);

    void getFloorPlans(@NonNull LoadFloorPlansCallback callback);
}
