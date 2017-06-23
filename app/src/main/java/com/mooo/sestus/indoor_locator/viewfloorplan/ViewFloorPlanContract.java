package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;
import com.mooo.sestus.indoor_locator.data.FingerPrintedLocation;
import com.mooo.sestus.indoor_locator.data.PointOfInterest;

import java.util.Collection;
import java.util.List;


public interface ViewFloorPlanContract {

    interface View extends BaseView<Presenter> {
        void showFloorPlanImage(String resourceName, Collection<PointF> pinnedLocations, Collection<PointF> floorPlanPois);

        void showConfirmAddPinToFloorPlan(PointF pin);

        void showConfirmAddPoiToFloorPlan(PointF pin);

        void showStartScanningActivity(int pinnedLocationId);

        void showPin(PointF pin);

        void showPoi(PointF pin);

        void showSelectedPin(PointF pin);

        void showSelectedPoi(PointF pin);

        void removePin(PointF awaitingPin);

        void removePoi(PointF awaitingPin);
    }

    interface Presenter extends BasePresenter {
        void onPointConfirmed();

        void onUserClickedFloorPlan(PointF pointF);

        void onUserLongClickedFloorPlan(PointF pointF);

        void scanSelectedPoint();
    }
}
