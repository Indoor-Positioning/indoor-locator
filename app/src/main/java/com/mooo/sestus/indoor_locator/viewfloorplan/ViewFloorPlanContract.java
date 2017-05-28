package com.mooo.sestus.indoor_locator.viewfloorplan;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;

/**
 * Created by mike on 5/27/17.
 */

public interface ViewFloorPlanContract {

    interface view extends BaseView<Presenter> {

        void setName(String name);

        void setDescription(String description);

        void startSelectFloorPlanActivity();

        void setDefaultFloorPlanPhoto();

        void setFloorPlanPhoto();

        void setThumbnailLoadingIndicator(boolean show);
    }

    interface Presenter extends BasePresenter {
        void onThumbnailClick();

        void onEditFloorPlanClick();

        void onThumbnailLoaded();

        void onFloorPlanExitClick();
    }
}
