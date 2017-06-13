package com.mooo.sestus.indoor_locator.scan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooo.sestus.indoor_locator.R;


public class MagneticScanFragment extends Fragment implements MagneticScanContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FP_ID = "FLOOR_PLAN_ID";
    private static final String POINT_ID = "POINT_ID";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MagneticScanContract.Presenter presenter;
    private TextView magneticX;
    private TextView magneticY;
    private TextView magneticZ;
    private TextView rotationX;
    private TextView rotationY;
    private TextView rotationZ;
    private FloatingActionButton startRecFab;
    private FloatingActionButton stopRecFab;

    public MagneticScanFragment() {
        // Required empty public constructor
    }


    public static MagneticScanFragment newInstance(String floorPlan, int point) {
        MagneticScanFragment fragment = new MagneticScanFragment();
        Bundle args = new Bundle();
        args.putString(FP_ID, floorPlan);
        args.putInt(POINT_ID, point);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(FP_ID);
            mParam2 = getArguments().getString(POINT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_magnetic_scan, container, false);
        magneticX = (TextView) v.findViewById(R.id.lbl_magnetic_x_val);
        magneticY = (TextView) v.findViewById(R.id.lbl_magnetic_y_val);
        magneticZ = (TextView) v.findViewById(R.id.lbl_magnetic_z_val);
        rotationX = (TextView) v.findViewById(R.id.lbl_rotation_x_val);
        rotationY = (TextView) v.findViewById(R.id.lbl_rotation_y_val);
        rotationZ = (TextView) v.findViewById(R.id.lbl_rotation_z_val);
        startRecFab = (FloatingActionButton) getActivity().findViewById(R.id.fab_start_rec);
        stopRecFab = (FloatingActionButton) getActivity().findViewById(R.id.fab_stop_rec);
        startRecFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startRecording();
                startRecFab.setVisibility(View.GONE);
                stopRecFab.setVisibility(View.VISIBLE);
            }
        });
        stopRecFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.stopRecording();
                stopRecFab.setVisibility(View.GONE);
                startRecFab.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }

    @Override
    public void setPresenter(MagneticScanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateAzimuth(float azimuth) {
        rotationX.setText(String.format("%.2f", azimuth));
    }

    @Override
    public void updatePitch(float pitch) {
        rotationY.setText(String.format("%.2f", pitch));
    }

    @Override
    public void updateRoll(float roll) {
        rotationZ.setText(String.format("%.2f", roll));
    }

    @Override
    public void updateMagneticFieldX(float fieldX) {
        magneticX.setText(String.format("%.2f", fieldX));
    }

    @Override
    public void updateMagneticFieldY(float fieldY) {
        magneticY.setText(String.format("%.2f", fieldY));
    }

    @Override
    public void updateMagneticFieldZ(float fieldZ) {
        magneticZ.setText(String.format("%.2f", fieldZ));
    }

    @Override
    public void updateMagneticAccuracy(int accuracy) {
        Snackbar.make(getView(), "Magnetic accuracy changed: " + accuracy, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateRotationVectorAccuracy(int accuracy) {
        Snackbar.make(getView(), "Rotation changed: " + accuracy, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddedFingerPrints(int fingerPrintsAdded) {
        Snackbar.make(getView(), String.format("Successfully added %d fingerprints", fingerPrintsAdded), Snackbar.LENGTH_LONG).show();
    }
}
