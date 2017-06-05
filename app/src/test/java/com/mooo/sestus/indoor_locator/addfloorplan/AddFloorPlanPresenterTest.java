package com.mooo.sestus.indoor_locator.addfloorplan;

import android.graphics.Bitmap;

import com.mooo.sestus.indoor_locator.data.FloorPlan;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddFloorPlanPresenterTest {

    @Mock
    FloorPlanRepository repository;

    @Mock
    AddFloorPlanContract.View view;

    @Captor
    private ArgumentCaptor<FloorPlanRepository.SaveFloorPlanCallback> saveFloorPlanCallback;

    private AddFloorPlanPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new AddFloorPlanPresenter(repository, view);
    }

    @Test
    public void SaveFloorPlan_WithInvalidName_ViewShowsError() {
        presenter.saveFloorPlan("", mock(Bitmap.class));

        verify(view).showEmptyNameError();
    }

    @Test
    public void SaveFloorPlan_WithNullPhoto_ViewShowsError() {
        presenter.saveFloorPlan("asd", null);

        verify(view).showEmptyPhotoError();
    }

    @Test
    public void SaveDuplicateFloorPlan_ViewShowsError() {
        when(repository.containsFloorPlan(eq("asd"), any(Bitmap.class))).thenReturn(true);
        presenter.saveFloorPlan("asd", mock(Bitmap.class));

        verify(view).showFloorPlanNameAlreadyExists();
    }


    @Test
    public void SaveFloorPlan_CallsRepositorySaveFloorPlan() {
        Bitmap bitmap = mock(Bitmap.class);
        presenter.saveFloorPlan("asd", bitmap);

        verify(repository).addFloorPlan(eq("asd"), eq(bitmap), any(FloorPlanRepository.SaveFloorPlanCallback.class));
    }

    @Test
    public void SaveFloorPlan_OnFloorPlanSaved_ViewFloorPlanIsCalled() {
        Bitmap bitmap = mock(Bitmap.class);
        FloorPlan floorPlan = mock(FloorPlan.class);
        when(floorPlan.getId()).thenReturn("Mock_id");
        presenter.saveFloorPlan("asd", bitmap);

        verify(repository).addFloorPlan(eq("asd"), eq(bitmap), saveFloorPlanCallback.capture());
        saveFloorPlanCallback.getValue().onFloorPlanSaved(floorPlan);
        verify(view).startViewFloorPlanActivity("Mock_id");
    }



}