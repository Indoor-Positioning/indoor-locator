package com.mooo.sestus.indoor_locator.addfloorplan;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooo.sestus.indoor_locator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFloorPlanFragment extends Fragment implements AddFloorPlanContract.View{


    private AddFloorPlanContract.Presenter presenter;
    private TextView name;
    private Uri photo;

    public AddFloorPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_floor_plan_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveFloorPlan(name.getText().toString(), photo);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_add_floor_plan, container, false);
        name = (TextView) root.findViewById(R.id.edt_add_floor_plan_name);
        setHasOptionsMenu(true);
        return root;
    }

    public static AddFloorPlanFragment newInstance() {
        return new AddFloorPlanFragment();
    }

    @Override
    public void setPresenter(AddFloorPlanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPickPhotoDialog() {

    }

    @Override
    public void showEmptyNameError() {
        Snackbar.make(name, "Invalid Floor Plan name", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showEmptyPhotoError() {

    }

    @Override
    public void startViewFloorPlanActivity(String floorPlanId) {

    }
}
