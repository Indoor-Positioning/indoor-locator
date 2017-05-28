package com.mooo.sestus.indoor_locator.selectfloorplan;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooo.sestus.indoor_locator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFloorPlanFragment extends Fragment {


    public SelectFloorPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_floor_plan, container, false);
    }

    public static SelectFloorPlanFragment newInstance() {
        return new SelectFloorPlanFragment();
    }
}
