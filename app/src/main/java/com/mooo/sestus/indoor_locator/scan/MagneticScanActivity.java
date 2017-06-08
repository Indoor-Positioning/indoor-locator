package com.mooo.sestus.indoor_locator.scan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mooo.sestus.indoor_locator.R;

public class MagneticScanActivity extends AppCompatActivity {

    public static final String FLOOR_PLAN_ID = "FP_ID";
    public static final String POINT_ID = "POINT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetic_scan);
    }
}
