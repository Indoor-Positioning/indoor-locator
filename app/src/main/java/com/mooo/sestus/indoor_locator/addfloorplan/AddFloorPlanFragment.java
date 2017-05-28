package com.mooo.sestus.indoor_locator.addfloorplan;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooo.sestus.indoor_locator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFloorPlanFragment extends Fragment {


    public AddFloorPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_floor_plan, container, false);
    }

}
