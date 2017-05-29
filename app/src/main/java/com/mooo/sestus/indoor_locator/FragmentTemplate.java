package com.mooo.sestus.indoor_locator;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class FragmentTemplate<Presenter extends BasePresenter> extends Fragment implements BaseView<Presenter>  {
    private Presenter presenter;

    public FragmentTemplate() {
        // Required empty public constructor
    }

//    public static FragmentTemplate newInstance() {
//        return new FragmentTemplate() {
//        }
//    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_floor_plan, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (presenter == null) {
            //presenter = new Presenter();
        }

        presenter.start();
    }

    @Override
    public void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }
}
