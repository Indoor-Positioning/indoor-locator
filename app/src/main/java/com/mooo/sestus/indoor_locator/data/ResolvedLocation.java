package com.mooo.sestus.indoor_locator.data;

public class ResolvedLocation {

    private int fingerPrintDistance;

    private int closestFingerPrintedLocation;

    private int closestPoi;

    public int getFingerPrintDistance() {
        return fingerPrintDistance;
    }

    public int getClosestFingerPrintedLocation() {
        return closestFingerPrintedLocation;
    }

    public int getClosestPoi() {
        return closestPoi;
    }
}
