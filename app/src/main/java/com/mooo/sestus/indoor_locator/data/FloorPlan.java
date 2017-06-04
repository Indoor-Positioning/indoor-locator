package com.mooo.sestus.indoor_locator.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Objects;

public class FloorPlan implements Comparable<FloorPlan> {
    private final String id;
    private final Bitmap image;

    public FloorPlan(String id, Bitmap image) {
        this.id = id;
        this.image = image;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
