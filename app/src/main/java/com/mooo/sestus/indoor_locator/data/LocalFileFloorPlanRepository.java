package com.mooo.sestus.indoor_locator.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.utils.CsvUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


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
        //TODO: Bulk - write the points to CSV, opening a new FileWriter on every added point is bad
        CsvUtils.savePointToCsv(propertiesMap.get(floorPlanId).getProperty("pointsPath"), pointF);
    }

    @Override
    public List<PointF> getFloorPlanPoints(String floorPlanId) {
        return floorPlanPoints.get(floorPlanId) == null ? Collections.<PointF>emptyList() : floorPlanPoints.get(floorPlanId);
    }

    @Override
    //TODO: if points list get too long, the indexOf method will be slow (O(n) on each lookup)
    public int getPointId(String floorPlanId, PointF point) {
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
    public boolean containsFloorPlan(String name) {
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
    public Map<Integer, List<Float[]>> getFingerPrints(String floorPlanId) {
        return fingerPrints.get(floorPlanId);
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
