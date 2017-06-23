package com.mooo.sestus.indoor_locator.data;

public class FingerPrintedLocation {

    private int id;

    private int floorPlanId;

    private int relatedPoi;

    private float xCoord;

    private float yCoord;

    private boolean isPoi;

    public FingerPrintedLocation(int floorPlanId, float xCoord, float yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floorPlanId = floorPlanId;
    }

    public FingerPrintedLocation() {}

    public int getId() {
        return id;
    }

    public float getYCoord() {
        return yCoord;
    }

    public float getXCoord() {
        return xCoord;
    }

    public boolean isPoi() {
        return isPoi;
    }

    public int getRelatedPoi() {
        return relatedPoi;
    }
}
