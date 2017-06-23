package com.mooo.sestus.indoor_locator.data;

public class FingerPrint {

    private int fingerPrintedLocationId;

    private float magneticX;

    private float magneticY;

    private float magneticZ;

    private float orientationX;

    private float orientationY;

    private float orientationZ;

    private float wifiRssi;


    public static FingerPrint clone(FingerPrint that) {
        FingerPrint fp = new FingerPrint();
        fp.magneticX = that.magneticX;
        fp.magneticY = that.magneticY;
        fp.magneticZ = that.magneticZ;
        fp.orientationX = that.orientationX;
        fp.orientationY = that.orientationY;
        fp.orientationZ = that.orientationZ;
        fp.wifiRssi = that.wifiRssi;
        fp.fingerPrintedLocationId = that.fingerPrintedLocationId;
        return fp;
    }

    public static FingerPrint createFor(float[] lastMeasurement) {
        FingerPrint fp = new FingerPrint();
        fp.magneticX = lastMeasurement[0];
        fp.magneticY = lastMeasurement[1];
        fp.magneticZ = lastMeasurement[2];
        fp.orientationX = lastMeasurement[3];
        fp.orientationY = lastMeasurement[4];
        fp.orientationZ = lastMeasurement[5];
        fp.wifiRssi = lastMeasurement[6];
        return fp;
    }

    public void add(FingerPrint fingerPrint) {
        magneticX += fingerPrint.magneticX;
        magneticY += fingerPrint.magneticY;
        magneticZ += fingerPrint.magneticZ;
        orientationX += fingerPrint.orientationX;
        orientationY += fingerPrint.orientationY;
        orientationZ += fingerPrint.orientationZ;
        wifiRssi += fingerPrint.wifiRssi;
    }

    public void divideBy(int divider) {
        magneticX /= divider;
        magneticY /= divider;
        magneticZ /= divider;
        orientationX /= divider;
        orientationY /= divider;
        orientationZ /= divider;
        wifiRssi /= divider;
    }

    public void clear() {
        magneticX = 0;
        magneticY = 0;
        magneticZ = 0;
        orientationX = 0;
        orientationY = 0;
        orientationZ = 0;
        wifiRssi = 0;
    }

    public void setFingerPrintedLocationId(int id) {
        this.fingerPrintedLocationId = id;
    }
}
