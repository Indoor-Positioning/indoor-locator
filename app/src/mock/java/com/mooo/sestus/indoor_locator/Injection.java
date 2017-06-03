package com.mooo.sestus.indoor_locator;

import android.content.Context;

import com.mooo.sestus.indoor_locator.data.FakeFloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;

public class Injection {
    public static FloorPlanRepository provideFloorPlanRepository(Context applicationContext) {
        return FakeFloorPlanRepository.getInstance();
    }
}
