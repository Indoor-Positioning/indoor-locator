package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;

import java.util.Collection;


public interface ViewFloorPlanContract {

    interface View extends BaseView<Presenter> {
        void showFloorPlanImage(Bitmap image, Collection<PointF> pinnedLocations);

        void showConfirmAddPinToFloorPlan(PointF pin);

        void showStartScanningActivity(String floorPlanId, int pinnedLocationId);

        void showPin(PointF pin);

        void showSelectedPin(PointF pin);

        void removePin(PointF awaitingPin);
    }

    interface Presenter extends BasePresenter {
        void onPinConfirmed();

        void recordFingerprints(PointF pin);

        void onUserClickedFloorPlan(PointF pointF);
    }
}
