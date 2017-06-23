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

    FloorPlan addFloorPlan(String name, Bitmap bitmap, SaveFloorPlanCallback callback);

    void getFloorPlans(@NonNull LoadFloorPlansCallback callback);

    FloorPlan getFloorPlanById(int floorPlanId);

    boolean containsFloorPlan(String name);

    void addFingerPrintedLocation(int floorPlanId, PointF pointF, AddFingerPrintedLocationCallback callback);

    void getFingerPrintedLocations(int floorPlanId, LoadFingerPrintedLocationsCallback callback);

    FingerPrintedLocation getFingerPrintedLocationById(int id);

    void addPoiToFloorPlan(int floorPlanId, PointF awaitingPin, AddPoiCallback callback);

    void getFloorPlanPois(int floorPlanId, LoadPointOfInterestsCallback callback);

    PointOfInterest getPointOfInterestById(int id);

    void addFingerPrint(FingerPrint fingerPrint);

    void saveFingerPrints();

    void getClosestPoint(int floorPlanId, FingerPrint fingerPrint, LocateCallback callback);

    interface SaveFloorPlanCallback {
        void onFloorPlanSaved(FloorPlan floorPlan);

        void onError();
    }

    interface LoadFloorPlansCallback {
        void onFloorPlansLoaded(Collection<FloorPlan> floorPlans);

        void onDataNotAvailable();
    }

    interface LoadFingerPrintedLocationsCallback {
        void onLocationsLoaded(Collection<FingerPrintedLocation> locations);

        void onDataNotAvailable();
    }

    interface LoadPointOfInterestsCallback {
        void onPoisLoaded(Collection<PointOfInterest> points);

        void onDataNotAvailable();
    }

    interface AddFingerPrintedLocationCallback {
        void onLocationAdded(FingerPrintedLocation location);

        void onError();
    }

    interface AddPoiCallback {
        void onPoiAdded(PointOfInterest poi);

        void onError();
    }

    interface LocateCallback {
        void onLocateResult(ResolvedLocation location);

        void onError();
    }
}
