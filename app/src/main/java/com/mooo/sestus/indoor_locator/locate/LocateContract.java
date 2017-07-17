package com.mooo.sestus.indoor_locator.locate;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;

import java.util.Collection;

public interface LocateContract {

    interface View extends BaseView<Presenter> {
        void showLocatedPointId(PointF point);

        void showNearestPoi(PointF pointF);

        void showIsOnPoi(PointF pointF, String name);

        void showDistance(String pointDistance);

        void showFloorPlanImage(String resourceName, Collection<PointF> pointsOfInterest);
    }

    interface Presenter extends BasePresenter {
        void startRecording();

        void stopRecording();
    }
}
