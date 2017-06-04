package com.mooo.sestus.indoor_locator.addfloorplan;

import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class AddFloorPlanPresenterTest {

    @Mock
    FloorPlanRepository repository;

    @Mock
    AddFloorPlanContract.View view;

    private AddFloorPlanPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new AddFloorPlanPresenter(repository, view);
    }

    @Test
    public void SaveFloorPlan_WithInvalidName_ViewShowsError() {
        presenter.saveFloorPlan("", null);

        verify(view).showEmptyNameError();
    }

    @Test
    public void SaveFloorPlan_WithNullPhoto_ViewShowsError() {
        presenter.saveFloorPlan("asd", null);

        verify(view).showEmptyPhotoError();
    }

}