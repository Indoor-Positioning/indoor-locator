package com.mooo.sestus.indoor_locator.viewfloorplan;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.selectfloorplan.SelectFloorPlanActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFloorPlanFragment extends Fragment implements ViewFloorPlanContract.view  {
    private ViewFloorPlanContract.Presenter presenter;

    private TextView floorPlanName, floorPlanDescription;
    private ImageView thumbnail;
    private FloatingActionButton editDetails;
    private ImageButton settins, exitFloorPlan;


    public ViewFloorPlanFragment() {
        // Required empty public constructor
    }

    public static ViewFloorPlanFragment newInstance() {
        return new ViewFloorPlanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_floor_plan, container, false);
        floorPlanName = (TextView) v.findViewById(R.id.lbl_floor_plan_name);
        floorPlanDescription = (TextView) v.findViewById(R.id.lbl_floor_plan_name);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (presenter == null) {
            presenter = new ViewFloorPlanPresenter();
        }

        presenter.subscribe();
    }

    @Override
    public void onDestroy() {
        presenter.unSubscribe();
        super.onDestroy();
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void startSelectFloorPlanActivity() {
        Intent i = new Intent(getActivity(), SelectFloorPlanActivity.class);
        startActivity(i);
    }

    @Override
    public void setDefaultFloorPlanPhoto() {

    }

    @Override
    public void setFloorPlanPhoto() {

    }

    @Override
    public void setThumbnailLoadingIndicator(boolean show) {

    }

    @Override
    public void setPresenter(ViewFloorPlanContract.Presenter presenter) {

    }

    @Override
    public void makeToast(@StringRes int stringId) {

    }

    @Override
    public void makeToast(String message) {

    }
}
