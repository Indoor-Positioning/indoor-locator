package com.mooo.sestus.indoor_locator.scan;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.mooo.sestus.indoor_locator.Injection;
import com.mooo.sestus.indoor_locator.R;

public class MagneticScanActivity extends AppCompatActivity {

    public static final String FLOOR_PLAN_ID = "FP_ID";
    public static final String POINT_ID = "POINT_ID";
    public static final String MAGNETIC_SCAN_FRAGMENT = "LOCATE_FRAGMENT";
    private MagneticScanContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetic_scan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int floorPlanId = getIntent().getIntExtra(FLOOR_PLAN_ID, -1);
        int pointId = getIntent().getIntExtra(POINT_ID, 0);
        FragmentManager fragManager = this.getSupportFragmentManager();
        MagneticScanFragment fragment = (MagneticScanFragment) fragManager.findFragmentByTag(MAGNETIC_SCAN_FRAGMENT);

        if (fragment == null) {
            fragment = MagneticScanFragment.newInstance(floorPlanId, pointId);
        }

        fragManager.beginTransaction()
                .replace(R.id.cont_magnetic_scan, fragment, MAGNETIC_SCAN_FRAGMENT)
                .commit();
        presenter = new MagneticScanPresenter(Injection.provideFloorPlanRepository(this), Injection.provideSensorRepository(this), fragment, pointId);
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
