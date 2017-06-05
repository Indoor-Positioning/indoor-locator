package com.mooo.sestus.indoor_locator.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mooo.sestus.indoor_locator.utils.CsvUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalFileFloorPlanRepository implements FloorPlanRepository {
    private static LocalFileFloorPlanRepository INSTANCE;
    private final File appDir;
    private TreeMap<String, FloorPlan> floorPlanMap;
    private HashMap<String, List<PointF>> floorPlanPoints;
    private HashMap<String, Properties> propertiesMap;

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
    public int getPinId(String floorplanId, PointF point) {
        return floorPlanPoints.get(floorplanId).indexOf(point);
    }

    @Override
    public void addFloorPlan(final String floorPlanName, final Bitmap bitmap, final SaveFloorPlanCallback callback) {
        final FloorPlan floorPlan = new FloorPlan(floorPlanName, bitmap);
        floorPlanMap.put(floorPlanName, floorPlan);
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
                callback.onFloorPlanSaved(floorPlan);
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
                    propertiesMap.put(name, floorPlanProps);
                    if (pointsPath != null && new File(pointsPath).exists()) {
                        List<PointF> points = CsvUtils.readCsv(new File(pointsPath));
                        floorPlanPoints.put(name, points);
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
        String pointsFileName = "points_" + floorPlanName + ".txt";
        String floorPlanPropsFile = floorPlanName + ".properties";
        File imagePath = new File(appDir, imageFileName);
        File pointsPath = new File(appDir, pointsFileName);
        File propsPath = new File(appDir, floorPlanPropsFile);
        properties.setProperty("name", floorPlanName);
        properties.setProperty("imagePath", imagePath.getAbsolutePath());
        properties.setProperty("pointsPath", pointsPath.getAbsolutePath());
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
