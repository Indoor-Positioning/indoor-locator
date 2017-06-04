package com.mooo.sestus.indoor_locator.selectfloorplan;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.addfloorplan.AddFloorPlanActivity;
import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.viewfloorplan.ViewFloorPlanActivity;
import java.util.SortedSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFloorPlanFragment extends Fragment implements SelectFloorPlanContract.View, android.view.View.OnClickListener {


    private SelectFloorPlanContract.Presenter presenter;
    private ArrayAdapter<FloorPlan> adapter;
    private Spinner spinner;
    private TextView noFloorPlansAvailableLabel;


    public SelectFloorPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        android.view.View root = inflater.inflate(R.layout.fragment_select_floor_plan, container, false);
        spinner = (Spinner)
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
    public void updateFloorPlanList(@NonNull final SortedSet<FloorPlan> floorPlanSet) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!floorPlanSet.isEmpty()) {
                    spinner.setVisibility(android.view.View.VISIBLE);
                    noFloorPlansAvailableLabel.setVisibility(android.view.View.INVISIBLE);
                    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, floorPlanSet.toArray(new FloorPlan[0]));
                    spinner.setAdapter(adapter);
                }
            }
        });
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
    public void onClick(android.view.View v) {
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
