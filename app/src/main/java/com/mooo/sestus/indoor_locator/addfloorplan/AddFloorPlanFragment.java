package com.mooo.sestus.indoor_locator.addfloorplan;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.viewfloorplan.ViewFloorPlanActivity;
import com.mvc.imagepicker.ImagePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFloorPlanFragment extends Fragment implements AddFloorPlanContract.View{


    private AddFloorPlanContract.Presenter presenter;
    private TextView name;
    private Bitmap photo;
    private ImageView imageView;
    private ImageView loadedImageView;

    public AddFloorPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_floor_plan_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveFloorPlan(name.getText().toString(), photo);
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imageView.setImageResource(R.mipmap.ic_pick_image);
                return true;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addPhoto();
            }
        });
        loadedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addPhoto();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_add_floor_plan, container, false);
        name = (TextView) root.findViewById(R.id.edt_add_floor_plan_name);
        imageView = ((ImageView) root.findViewById(R.id.result_image));
        loadedImageView = (ImageView) root.findViewById(R.id.loaded_image);
        setHasOptionsMenu(true);
        return root;
    }

    public static AddFloorPlanFragment newInstance() {
        return new AddFloorPlanFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photo = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
        imageView.setVisibility(View.GONE);
        loadedImageView.setImageBitmap(photo);
        loadedImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(AddFloorPlanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPickPhotoDialog() {
        ImagePicker.pickImage(getActivity(), "Select your image:");
    }

    @Override
    public void showEmptyNameError() {
        Snackbar.make(name, "Invalid Floor Plan name", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showEmptyPhotoError() {
        Snackbar.make(name, "Please select a valid floor plan photo", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void startViewFloorPlanActivity(String floorPlanId) {
        Intent intent = new Intent(getContext(), ViewFloorPlanActivity.class);
        intent.putExtra(ViewFloorPlanActivity.FLOOR_PLAN_ID, floorPlanId);
        startActivity(intent);
    }

    @Override
    public void showErrorOnSavingFloorPlan() {
        Snackbar.make(name, "Failed to save floor plan... Please try again", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showFloorPlanNameAlreadyExists() {
        Snackbar.make(name, "Floor Plan name already exists", Snackbar.LENGTH_LONG).show();
    }
}
