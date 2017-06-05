package com.mooo.sestus.indoor_locator.viewfloorplan;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mooo.sestus.indoor_locator.Injection;
import com.mooo.sestus.indoor_locator.R;

public class ViewFloorPlanActivity extends AppCompatActivity {
    private static final String VIEW_FLOOR_PLAN_FRAGMENT = "VIEW_FLOOR_PLAN_FRAGMENT";
    public static final String FLOOR_PLAN_ID = "FLOOR_PLAN_ID";

    private FragmentManager fragManager;
    private ViewFloorPlanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_floor_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String floorPlanId = getIntent().getStringExtra(FLOOR_PLAN_ID);
        fragManager = this.getSupportFragmentManager();
        ViewFloorPlanFragment fragment = (ViewFloorPlanFragment) fragManager.findFragmentByTag(VIEW_FLOOR_PLAN_FRAGMENT);
        if (fragment == null) {
            fragment = ViewFloorPlanFragment.newInstance(floorPlanId);
        }

        fragManager.beginTransaction()
                .replace(R.id.cont_view_floor_plan_fragment, fragment, VIEW_FLOOR_PLAN_FRAGMENT)
                .commit();

        presenter = new ViewFloorPlanPresenter(Injection.provideFloorPlanRepository(getApplicationContext()), fragment, floorPlanId);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }
}
