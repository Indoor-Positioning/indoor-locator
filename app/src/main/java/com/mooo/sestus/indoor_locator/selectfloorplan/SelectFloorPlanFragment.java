package com.mooo.sestus.indoor_locator.selectfloorplan;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.addfloorplan.AddFloorPlanActivity;
import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.viewfloorplan.ViewFloorPlanActivity;

import java.util.SortedSet;

import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFloorPlanFragment extends Fragment implements SelectFloorPlanContract.view, View.OnClickListener {


    private SelectFloorPlanContract.Presenter presenter;
    private ArrayAdapter<FloorPlan> adapter;
    private MaterialSpinner spinner;
    private TextView noFloorPlansAvailableLabel;

    public SelectFloorPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_select_floor_plan, container, false);
        spinner = (MaterialSpinner)
                root.findViewById(R.id.floor_plans_spinner);
        noFloorPlansAvailableLabel = (TextView) root.findViewById(R.id.no_floor_plans);
        Button selectFloorPlanButton = (Button) root.findViewById(R.id.btn_select_floor_plan);
        selectFloorPlanButton.setOnClickListener(this);
        Button addNewFloorPlanButton = (Button) root.findViewById(R.id.btn_add_floor_plan);
        addNewFloorPlanButton.setOnClickListener(this);

        return root;
    }

    public static SelectFloorPlanFragment newInstance() {
        return new SelectFloorPlanFragment();
    }

    @Override
    public void setPresenter(SelectFloorPlanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator() {

    }

    @Override
    public void showErrorOnLoadingFloorPlans() {
        Snackbar.make(getView(), "Failed to load Floor Plans", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateFloorPlanList(@NonNull SortedSet<FloorPlan> floorPlanList) {
        if (!floorPlanList.isEmpty()) {
            spinner.setVisibility(View.VISIBLE);
            noFloorPlansAvailableLabel.setVisibility(View.INVISIBLE);
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, (FloorPlan[]) floorPlanList.toArray());
            spinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void startViewFloorPlanActivity(String floorPlanId) {
        Intent intent = new Intent(getContext(), ViewFloorPlanActivity.class);
        intent.putExtra(ViewFloorPlanActivity.FLOOR_PLAN_ID, floorPlanId);
        startActivity(intent);
    }

    @Override
    public void startAddFloorPlanActivity() {
        Intent intent = new Intent(getContext(), AddFloorPlanActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_select_floor_plan) {
            int selectedItemPos = spinner.getSelectedItemPosition();
            if (selectedItemPos != AdapterView.INVALID_POSITION) {
                FloorPlan floorPlan = adapter.getItem(selectedItemPos);
                if (floorPlan != null)
                    presenter.viewFloorPlan(floorPlan);
            }
        }
        else if (v.getId() == R.id.btn_add_floor_plan) {
            presenter.addNewFloorPlan();
        }
    }
}
