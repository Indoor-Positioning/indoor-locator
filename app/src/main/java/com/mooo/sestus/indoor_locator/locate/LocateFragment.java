package com.mooo.sestus.indoor_locator.locate;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.viewfloorplan.PinView;


public class LocateFragment extends Fragment implements LocateContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FP_ID = "FLOOR_PLAN_ID";
    private static final String POINT_ID = "POINT_ID";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LocateContract.Presenter presenter;
    private PinView floorPlanImage;
    private TextView distanceTextView;


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
        floorPlanImage = (PinView) v.findViewById(R.id.imageView);
        distanceTextView = (TextView) v.findViewById(R.id.lbl_distance_val);

        return v;
    }

    @Override
    public void setPresenter(LocateContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showLocatedPointId(final PointF point) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floorPlanImage.addLocatedPin(point);
            }
        });
    }

    @Override
    public void showDistance(final String poindDistance) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                distanceTextView.setText(poindDistance);
            }
        });;

    }
    public void showFloorPlanImage(Bitmap image) {
        floorPlanImage.setImage(ImageSource.cachedBitmap(image));
    }
}
