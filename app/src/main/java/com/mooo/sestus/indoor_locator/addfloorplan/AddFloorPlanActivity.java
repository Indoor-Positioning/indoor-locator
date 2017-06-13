package com.mooo.sestus.indoor_locator.addfloorplan;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mooo.sestus.indoor_locator.Injection;
import com.mooo.sestus.indoor_locator.R;

public class AddFloorPlanActivity extends AppCompatActivity {

    private static final String ADD_FLOOR_PLAN_FRAGMENT = "ADD_FLOOR_PLAN";
    private AddFloorPlanContract.Presenter presenter;
    private AddFloorPlanFragment fragment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_floor_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragManager = this.getSupportFragmentManager();
        fragment = (AddFloorPlanFragment) fragManager.findFragmentByTag(ADD_FLOOR_PLAN_FRAGMENT);

        if (fragment == null) {
            fragment = AddFloorPlanFragment.newInstance();
        }

        fragManager.beginTransaction()
                .replace(R.id.cont_add_floorplan, fragment, ADD_FLOOR_PLAN_FRAGMENT)
                .commit();

        presenter = new AddFloorPlanPresenter(Injection.provideFloorPlanRepository(getApplicationContext()), fragment);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
