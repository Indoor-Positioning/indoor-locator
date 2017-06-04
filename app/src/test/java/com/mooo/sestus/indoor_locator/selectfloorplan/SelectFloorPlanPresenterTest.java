package com.mooo.sestus.indoor_locator.selectfloorplan;

import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectFloorPlanPresenterTest {

    @Mock
    FloorPlanRepository repository;

    @Mock
    SelectFloorPlanContract.View view;

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

    @Test
    public void PresenterStopped_WhileLoadIngFloorPlans_ViewIsNotCalled() {
        presenter.start();

        verify(repository).getFloorPlans(loadFloorPlansCallback.capture());
        presenter.stop();
        loadFloorPlansCallback.getValue().onFloorPlansLoaded(null);

        verify(view, never()).updateFloorPlanList(null);
    }

    @Test
    public void PresenterStopped_WhileLoadIngFloorPlans_ViewShowErrorIsNotCalled() {
        presenter.start();

        verify(repository).getFloorPlans(loadFloorPlansCallback.capture());
        presenter.stop();
        loadFloorPlansCallback.getValue().onDataNotAvailable();

        verify(view, never()).showErrorOnLoadingFloorPlans();
    }

    @Test
    public void AddNewFloorPlan_ViewStartAddFloorPlanActivityIsCalled() {
        presenter.addNewFloorPlan();

        verify(view).startAddFloorPlanActivity();
    }

    @Test
    public void ViewFloorPlan_ViewStartViewFloorPlanActivityIsCalled() {
        FloorPlan floorPlan = mock(FloorPlan.class);
        when(floorPlan.getId()).thenReturn("Floor plan id");
        presenter.viewFloorPlan(floorPlan);

        verify(view).startViewFloorPlanActivity("Floor plan id");
    }

}