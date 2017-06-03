package com.mooo.sestus.indoor_locator.data;

import android.support.annotation.NonNull;

public class FloorPlan implements Comparable<FloorPlan> {
    private final String id;

    public FloorPlan(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(@NonNull FloorPlan o) {
        return id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
