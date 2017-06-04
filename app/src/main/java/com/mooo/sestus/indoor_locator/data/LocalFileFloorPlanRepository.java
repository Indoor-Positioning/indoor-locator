package com.mooo.sestus.indoor_locator.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocalFileFloorPlanRepository implements FloorPlanRepository {
    private static LocalFileFloorPlanRepository INSTANCE;
    private final File appDir;
    private TreeSet<FloorPlan> floorPlanSet;

    private LocalFileFloorPlanRepository(@NonNull Context context) {
        appDir = context.getFilesDir();
    }

    public static LocalFileFloorPlanRepository getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new LocalFileFloorPlanRepository(context);
        return INSTANCE;
    }

    @Override
    public void saveFloorPlan(final String floorPlanName, final Bitmap bitmap, final SaveFloorPlanCallback callback) {
        final FloorPlan floorPlan = new FloorPlan(floorPlanName, bitmap);
        floorPlanSet.add(floorPlan);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Properties properties = new Properties();
                FileOutputStream fos = null;
                String imageFileName = "img_" + floorPlanName + ".png";
                String floorPlanPropsFile = floorPlanName + ".properties";
                File imagePath = new File(appDir, imageFileName);
                File propsPath = new File(appDir, floorPlanPropsFile);
                properties.setProperty("name", floorPlanName);
                properties.setProperty("imagePath", imagePath.getAbsolutePath());
                try {
                    fos = new FileOutputStream(imagePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        FileOutputStream out = new FileOutputStream(propsPath);
                        properties.store(out, "Floor Plan Properties");
                        out.close();
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
        FloorPlan floorPlan = new FloorPlan(name, bitmap);
        return floorPlanSet.contains(floorPlan);
    }

    @Override
    public void getFloorPlans(@NonNull final FloorPlanRepository.LoadFloorPlansCallback callback) {
        if (floorPlanSet == null)
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    reloadFloorPlans();
                    callback.onFloorPlansLoaded(floorPlanSet);
                }
            });
        else
            callback.onFloorPlansLoaded(floorPlanSet);
    }

    private void reloadFloorPlans() {
        floorPlanSet = new TreeSet<>();
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
                    File imageFile = new File(imagePath);
                    Bitmap image = null;
                    try {
                        image = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (image != null)
                        floorPlanSet.add(new FloorPlan(name, image));
                }
            }
        }
    }
}
