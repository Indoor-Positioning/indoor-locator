package com.mooo.sestus.indoor_locator.selectfloorplan;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mooo.sestus.indoor_locator.R;

import fr.ganfra.materialspinner.MaterialSpinner;


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
}
