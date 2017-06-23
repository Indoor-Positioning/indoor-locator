package com.mooo.sestus.indoor_locator.data;

public class PointOfInterest {

    private int id;

    private String name;

    private int floorPlanId;

    private float xCoord;

    private float yCoord;

    private int relatedFingerPrintedLocId;


    public PointOfInterest(int floorPlanId, float xCoord, float yCoord) {
        this.floorPlanId = floorPlanId;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public PointOfInterest() {}

    public int getId() {
        return id;
    }

    public float getYCoord() {
        return yCoord;
    }

    public float getXCoord() {
        return xCoord;
    }

    public int getRelatedFingerPrintedLocId() {
        return relatedFingerPrintedLocId;
    }
}
