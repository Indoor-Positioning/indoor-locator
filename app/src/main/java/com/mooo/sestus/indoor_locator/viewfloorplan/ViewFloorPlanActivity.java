package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mooo.sestus.indoor_locator.R;

public class ViewFloorPlanActivity extends AppCompatActivity {
    private static final String VIEW_FLOOR_PLAN_FRAGMENT = "VIEW_FLOOR_PLAN_FRAGMENT";
    public static final String FLOOR_PLAN_ID = "FLOOR_PLAN_ID";

    private FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_floor_plan);
//        getActionBar().setTitle("Floor Plan");

        fragManager = this.getFragmentManager();
        ViewFloorPlanFragment fragment = (ViewFloorPlanFragment) fragManager.findFragmentByTag(VIEW_FLOOR_PLAN_FRAGMENT);

        if (fragment == null) {
            fragment = ViewFloorPlanFragment.newInstance();

        }

        fragManager.beginTransaction()
                .replace(R.id.cont_view_floor_plan_fragment, fragment, VIEW_FLOOR_PLAN_FRAGMENT)
                .commit();

    }
}
