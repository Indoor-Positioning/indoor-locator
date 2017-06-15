package com.mooo.sestus.indoor_locator.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mooo.sestus.indoor_locator.utils.CsvUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalFileFloorPlanRepository implements FloorPlanRepository {
    private static LocalFileFloorPlanRepository INSTANCE;
    private final File appDir;
    private Map<String, FloorPlan> floorPlanMap;
    private Map<String, Map<Integer, List<Float[]>>> fingerPrints = new HashMap<>();
    private Map<String, List<PointF>> floorPlanPoints;
    private Map<String, Properties> propertiesMap;

    private LocalFileFloorPlanRepository(@NonNull Context context) {
        appDir = context.getFilesDir();
    }

    public static LocalFileFloorPlanRepository getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new LocalFileFloorPlanRepository(context);
        return INSTANCE;
    }

    @Override
    public void addPointToFloorPlan(String floorPlanId, PointF pointF) {
        List<PointF> points = floorPlanPoints.get(floorPlanId);
        if (points == null)
            floorPlanPoints.put(floorPlanId, points = new ArrayList<>());
        points.add(pointF);
        CsvUtils.writeToCsv(propertiesMap.get(floorPlanId).getProperty("pointsPath"), pointF);
    }

    @Override
    public List<PointF> getFloorPlanPoints(String floorPlanId) {
        return floorPlanPoints.get(floorPlanId) == null ? Collections.<PointF>emptyList() : floorPlanPoints.get(floorPlanId);
    }

    @Override
    public int getPinId(String floorPlanId, PointF point) {
        return floorPlanPoints.get(floorPlanId).indexOf(point);
    }

    @Override
    public void addFloorPlan(final String floorPlanName, final Bitmap bitmap, final SaveFloorPlanCallback callback) {
        final FloorPlan floorPlan = new FloorPlan(floorPlanName, bitmap);
        floorPlanMap.put(floorPlanName, floorPlan);
        callback.onFloorPlanSaved(floorPlan);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                Properties properties = new Properties();
                setProperties(properties, floorPlanName);
                propertiesMap.put(floorPlanName, properties);
                try {
                    fos = new FileOutputStream(properties.getProperty("imagePath"));
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public boolean containsFloorPlan(String name, Bitmap bitmap) {
        return floorPlanMap.containsKey(name);
    }

    @Override
    public FloorPlan getFloorPlanById(String floorPlanId) {
        return floorPlanMap.get(floorPlanId);
    }

    @Override
    public void addFingerPrint(String floorPlanId, int pointId, float[] measurement) {
        Map<Integer, List<Float[]>> floorPlanEntry = fingerPrints.get(floorPlanId);
        if (floorPlanEntry == null) {
            floorPlanEntry = new HashMap<>();
            fingerPrints.put(floorPlanId, floorPlanEntry);
            List<Float[]> values = new ArrayList<>();
            floorPlanEntry.put(pointId, values);
        }
        List<Float[]> values = floorPlanEntry.get(pointId);
        if (values == null) {
            values = new ArrayList<>();
            floorPlanEntry.put(pointId,values);
        }
        values.add(ArrayUtils.toObject(measurement));
    }

    @Override
    public void saveFingerPrints(String floorPlanId, int pointId) {
        List<Float[]> values = fingerPrints.get(floorPlanId).get(pointId);
        CsvUtils.saveFingerPrintsToCsv(propertiesMap.get(floorPlanId).getProperty("fingerPrintsPath"), pointId, values);
    }

    @Override
    public int getClosestPoint(float[] measurement, String floorPlanId) {
        if (fingerPrints.get(floorPlanId) == null)
            return -1;
        Set<Map.Entry<Integer, List<Float[]>>> entrySet = fingerPrints.get(floorPlanId).entrySet();
        Map<Integer, List<Integer>> distances = new HashMap<>();
        Log.v("LOCATE", String.format("Locating fingerprint %s on %d points", Arrays.toString(measurement), entrySet.size()));
        for (Map.Entry<Integer, List<Float[]>> entry : entrySet) {
            List<Integer> pointDistances = new ArrayList<>(entry.getValue().size());
            distances.put(entry.getKey(), pointDistances);
            Log.v("LOCATE", String.format("Trying Point %d which has %d fingerprints", entry.getKey(), entry.getValue().size()));
            for (Float[] fingerprint: entry.getValue()) {
                int newDistance = computeDistance(fingerprint, measurement);
                pointDistances.add(newDistance);
            }
        }

        return computeClosestPoint(distances);
    }

    private int computeClosestPoint(Map<Integer, List<Integer>> distances) {
        int minSumOfFiveDistance = Integer.MAX_VALUE;
        int closestPoint = -1;
        for (Map.Entry<Integer, List<Integer>> entry : distances.entrySet()) {
            List<Integer> pointDistances = entry.getValue();
            Collections.sort(pointDistances);
            int pointMinOfFiveSum = 0;
            for (int i = 0; i < 5; i++)
                pointMinOfFiveSum += pointDistances.get(i);
            if (pointMinOfFiveSum < minSumOfFiveDistance) {
                minSumOfFiveDistance = pointMinOfFiveSum;
                closestPoint = entry.getKey();
            }
        }
        if (minSumOfFiveDistance >= 105)
            return -1;
        return closestPoint;
    }

    private int computeDistance(Float[] fingerprint, float[] measurement) {
        float distance = 0;
        for (int i = 0; i < 3; i++) {
            distance += Math.abs(fingerprint[i] - measurement[i]);
        }
        distance += 0.6 * Math.abs(fingerprint[3] - measurement[3]);
        return (int) distance;
    }

    @Override
    public void getFloorPlans(@NonNull final FloorPlanRepository.LoadFloorPlansCallback callback) {
        if (floorPlanMap == null)
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    reloadFloorPlans();
                    callback.onFloorPlansLoaded(floorPlanMap.values());
                }
            });
        else
            callback.onFloorPlansLoaded(floorPlanMap.values());
    }

    private void reloadFloorPlans() {
        floorPlanMap = new TreeMap<>();
        propertiesMap = new HashMap<>();
        floorPlanPoints = new HashMap<>();
        File[] files = appDir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".properties")) {
                Properties floorPlanProps = new Properties();
                try {
                    InputStream is = new FileInputStream(file);
                    floorPlanProps.load(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!floorPlanProps.isEmpty()) {
                    String name = floorPlanProps.getProperty("name");
                    String imagePath = floorPlanProps.getProperty("imagePath");
                    String pointsPath = floorPlanProps.getProperty("pointsPath");
                    String fingerPrintsPath = floorPlanProps.getProperty("fingerPrintsPath");
                    propertiesMap.put(name, floorPlanProps);
                    if (pointsPath != null && new File(pointsPath).exists()) {
                        List<PointF> points = CsvUtils.loadFloorPlanFromCsv(new File(pointsPath));
                        floorPlanPoints.put(name, points);
                    }
                    if (fingerPrintsPath!= null && new File(fingerPrintsPath).exists()) {
                        fingerPrints.put(name, CsvUtils.loadFingerPrintsFromCsv(new File(fingerPrintsPath)));
                    }
                    File imageFile = new File(imagePath);
                    Bitmap image = null;
                    try {
                        image = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (image != null)
                        floorPlanMap.put(name, new FloorPlan(name, image));
                }
            }
        }
    }

    private void setProperties(Properties properties, String floorPlanName) {
        String imageFileName = "img_" + floorPlanName + ".png";
        String pointsFileName = "points_" + floorPlanName + ".csv";
        String fingerPrintsFileName = "fingerprints_" + floorPlanName + ".csv";
        String floorPlanPropsFile = floorPlanName + ".properties";
        File imagePath = new File(appDir, imageFileName);
        File pointsPath = new File(appDir, pointsFileName);
        File propsPath = new File(appDir, floorPlanPropsFile);
        File fingerPrintsPath = new File(appDir, fingerPrintsFileName);
        properties.setProperty("name", floorPlanName);
        properties.setProperty("imagePath", imagePath.getAbsolutePath());
        properties.setProperty("pointsPath", pointsPath.getAbsolutePath());
        properties.setProperty("fingerPrintsPath", fingerPrintsPath.getAbsolutePath());
        try {
            FileOutputStream out = new FileOutputStream(propsPath);
            properties.store(out, "Floor Plan Properties");
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




}
