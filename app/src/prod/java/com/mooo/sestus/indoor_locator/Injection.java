/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mooo.sestus.indoor_locator;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.data.DeviceSensorRepository;
import com.mooo.sestus.indoor_locator.data.FloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.RemoteFloorPlanRepository;
import com.mooo.sestus.indoor_locator.data.SensorRepository;

import static com.google.common.base.Preconditions.checkNotNull;


public class Injection {

    public static FloorPlanRepository provideFloorPlanRepository(@NonNull Context context) {
        checkNotNull(context);
        return RemoteFloorPlanRepository.getInstance();
    }

    public static SensorRepository provideSensorRepository(@NonNull Activity activity) {
        return DeviceSensorRepository.getInstance(activity);
    }
}
