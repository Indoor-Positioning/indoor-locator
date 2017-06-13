package com.mooo.sestus.indoor_locator.locate;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooo.sestus.indoor_locator.R;


public class LocateFragment extends Fragment implements LocateContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FP_ID = "FLOOR_PLAN_ID";
    private static final String POINT_ID = "POINT_ID";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LocateContract.Presenter presenter;
    private TextView magneticX;
    private TextView magneticY;
    private TextView magneticZ;
    private TextView rotationX;
    private TextView rotationY;
    private TextView rotationZ;
    private TextView locatedPoint;


    public LocateFragment() {
        // Required empty public constructor
    }


    public static LocateFragment newInstance(String floorPlan) {
        LocateFragment fragment = new LocateFragment();
        Bundle args = new Bundle();
        args.putString(FP_ID, floorPlan);
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
        View v = inflater.inflate(R.layout.fragment_locate, container, false);
        magneticX = (TextView) v.findViewById(R.id.lbl_magnetic_x_val);
        magneticY = (TextView) v.findViewById(R.id.lbl_magnetic_y_val);
        magneticZ = (TextView) v.findViewById(R.id.lbl_magnetic_z_val);
        rotationX = (TextView) v.findViewById(R.id.lbl_rotation_x_val);
        rotationY = (TextView) v.findViewById(R.id.lbl_rotation_y_val);
        rotationZ = (TextView) v.findViewById(R.id.lbl_rotation_z_val);
        locatedPoint = (TextView) v.findViewById(R.id.lbl_located_point_val);

        return v;
    }

    @Override
    public void setPresenter(LocateContract.Presenter presenter) {
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
    public void showLocatedPointId(final int pointId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                locatedPoint.setText(String.format("Point %d", pointId));
            }
        });
    }
}
