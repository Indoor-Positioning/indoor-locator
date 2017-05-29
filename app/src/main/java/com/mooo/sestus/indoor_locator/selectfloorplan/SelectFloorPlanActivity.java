package com.mooo.sestus.indoor_locator.selectfloorplan;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mooo.sestus.indoor_locator.Injection;
import com.mooo.sestus.indoor_locator.R;

public class SelectFloorPlanActivity extends AppCompatActivity {

    private static final String SELECT_FLOOR_PLAN_FRAGMENT = "SELECT_FLOOR_PLAN_TAG";
    private SelectFloorPlanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_floor_plan);
        getSupportActionBar().setTitle(R.string.header_wellcome);

        FragmentManager fragManager = this.getFragmentManager();
        SelectFloorPlanFragment fragment = (SelectFloorPlanFragment) fragManager.findFragmentByTag(SELECT_FLOOR_PLAN_FRAGMENT);

        if (fragment == null) {
            fragment = SelectFloorPlanFragment.newInstance();
        }

        fragManager.beginTransaction()
                .replace(R.id.cont_select_floor_plan, fragment, SELECT_FLOOR_PLAN_FRAGMENT)
                .commit();

        presenter = new SelectFloorPlanPresenter(Injection.provideFloorPlanRepository(getApplicationContext()), fragment);

    }
}
