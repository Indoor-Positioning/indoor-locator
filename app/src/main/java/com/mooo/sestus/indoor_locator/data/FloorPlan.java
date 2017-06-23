package com.mooo.sestus.indoor_locator.data;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * TODO: Standardize, error-check the FloorPlan id which is currently a simple String.
 */
public class FloorPlan implements Comparable<FloorPlan> {
    private final int id;
    private final String name;
    private final String resourceName;

    public FloorPlan(int id, String name, String resourceName) {
        this.id = id;
        this.name = name;
        this.resourceName = resourceName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NonNull FloorPlan o) {
        return id > o.id ? 1 : id == o.id ? 0 : -1;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public String getResourceName() {
        return resourceName;
    }
}
