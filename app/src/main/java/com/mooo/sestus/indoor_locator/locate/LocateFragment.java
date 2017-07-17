package com.mooo.sestus.indoor_locator.locate;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.google.common.base.Strings;
import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.viewfloorplan.PinView;

import java.util.Collection;
import java.util.Locale;


public class LocateFragment extends Fragment implements LocateContract.View {


    private LocateContract.Presenter presenter;
    private PinView floorPlanImage;
    private TextView distanceTextView;


    public LocateFragment() {
        // Required empty public constructor
    }


    public static LocateFragment newInstance(int floorPlan) {
        return new LocateFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_locate, container, false);
        floorPlanImage = (PinView) v.findViewById(R.id.imageView);
        distanceTextView = (TextView) v.findViewById(R.id.lbl_distance_val);

        return v;
    }

    @Override
    public void setPresenter(LocateContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showLocatedPointId(final PointF point) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floorPlanImage.addLocatedPin(point);
            }
        });
    }

    @Override
    public void showNearestPoi(final PointF pointF) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floorPlanImage.setNearByPoi(pointF);
            }
        });
    }

    @Override
    public void showIsOnPoi(final PointF pointF, final String name) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floorPlanImage.setActivePoi(pointF);
                if (!Strings.isNullOrEmpty(name))
                    Snackbar.make(getView(), String.format(Locale.getDefault(), "Reached POI : %s", name), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showDistance(final String poindDistance) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                distanceTextView.setText(poindDistance);
            }
        });

    }

    @Override
    public void showFloorPlanImage(final String resourceName, final Collection<PointF> pointsOfInterest) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int resourceId = getContext().getResources().getIdentifier(resourceName, "drawable", getContext().getPackageName());
                floorPlanImage.setImage(ImageSource.resource(resourceId));
                floorPlanImage.setPois(pointsOfInterest);
            }
        });

    }
}
