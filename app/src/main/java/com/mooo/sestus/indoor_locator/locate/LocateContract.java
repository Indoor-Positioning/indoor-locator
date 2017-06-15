package com.mooo.sestus.indoor_locator.locate;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;

public interface LocateContract {

    interface View extends BaseView<Presenter> {
        void showLocatedPointId(PointF point);

        void showFloorPlanImage(Bitmap image);
    }

    interface Presenter extends BasePresenter {
        void startRecording();

        void stopRecording();
    }
}
