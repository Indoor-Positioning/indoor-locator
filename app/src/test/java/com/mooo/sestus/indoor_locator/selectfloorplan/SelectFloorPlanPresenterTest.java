package com.mooo.sestus.indoor_locator.selectfloorplan;

import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class SelectFloorPlanPresenterTest {

    @Mock
    FloorPlanRepository repository;

    @Mock
    SelectFloorPlanContract.view view;

    @Captor
    private ArgumentCaptor<FloorPlanRepository.LoadFloorPlansCallback> loadFloorPlansCallback;

    private SelectFloorPlanPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new SelectFloorPlanPresenter(repository, view);
    }


    @Test
    public void NewPresenter_SetsPresenterToView() {
        presenter = new SelectFloorPlanPresenter(repository, view);

        verify(view).setPresenter(presenter);
    }

    @Test
    public void Start_LoadsFloorPlanFromRepository() {
        presenter.start();

        verify(repository).getFloorPlans(any(FloorPlanRepository.LoadFloorPlansCallback.class));
    }

    @Test
    public void FloorPlansLoaded_ViewUpdateFloorPlanIsCalled() {
        presenter.start();

        verify(repository).getFloorPlans(loadFloorPlansCallback.capture());
        SortedSet<FloorPlan> floorPlans = new TreeSet<>();

        loadFloorPlansCallback.getValue().onFloorPlansLoaded(floorPlans);

        verify(view).updateFloorPlanList(floorPlans);
    }

    @Test
    public void LoadFloorPlansError_ViewShowErrorIsCalled() {
        presenter.start();

        verify(repository).getFloorPlans(loadFloorPlansCallback.capture());
        loadFloorPlansCallback.getValue().onDataNotAvailable();

        verify(view).showErrorOnLoadingFloorPlans();
    }

}