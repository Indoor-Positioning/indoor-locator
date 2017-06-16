package com.mooo.sestus.indoor_locator.data;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;


/**
 * This is supposed to be an abstracted repository to obtain floorplan, point, fingerprint info.
 * The current implementation, LocalileFloorPlanRepository stores the above info in local csv files.
 * Hypothetically, we could swap this repository with a RemoteFloorPlanRepository on a remote server.
 * TODO: Define a more clear interface, remove unnecessary methods, be consistent on callbacks.
 */
public interface FloorPlanRepository {

    void addFloorPlan(String name, Bitmap bitmap, SaveFloorPlanCallback callback);

    void getFloorPlans(@NonNull LoadFloorPlansCallback callback);

    FloorPlan getFloorPlanById(String floorPlanId);

    boolean containsFloorPlan(String name);


    void addPointToFloorPlan(String floorPlanId, PointF pointF);

    List<PointF> getFloorPlanPoints(String floorPlanId);

    int getPointId(String floorplanId, PointF point);


    void addFingerPrint(String floorPlanId, int pointId, float[] measurements);

    void saveFingerPrints(String floorPlanId, int pointId);

    Map<Integer, List<Float[]>> getFingerPrints(String floorPlanId);

    interface SaveFloorPlanCallback {
        void onFloorPlanSaved(FloorPlan floorPlan);

        void onError();
    }

    interface LoadFloorPlansCallback {
        void onFloorPlansLoaded(Collection<FloorPlan> floorPlans);

        void onDataNotAvailable();
    }
}
