package com.mooo.sestus.indoor_locator.selectfloorplan;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.data.FloorPlan;

import java.util.List;
import java.util.SortedSet;

import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFloorPlanFragment extends Fragment implements SelectFloorPlanContract.view{


    public SelectFloorPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_select_floor_plan, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[]{"<No Floor Plans Available>"});
        MaterialSpinner textView = (MaterialSpinner)
                root.findViewById(R.id.floor_plans_spinner);
        textView.setAdapter(adapter);

        return root;

    }

    public static SelectFloorPlanFragment newInstance() {
        return new SelectFloorPlanFragment();
    }

    @Override
    public void setPresenter(SelectFloorPlanContract.Presenter presenter) {

    }

    @Override
    public void makeToast(@StringRes int stringId) {

    }

    @Override
    public void makeToast(String message) {

    }

    @Override
    public void setLoadingIndicator() {

    }

    @Override
    public void showErrorOnLoadingFloorPlans() {

    }

    @Override
    public void updateFloorPlanList(SortedSet<FloorPlan> floorPlanList) {

    }

    @Override
    public void startViewFloorPlanActivity(String floorPlanId) {

    }

    @Override
    public void startAddFloorPlanActivity() {

    }
}
