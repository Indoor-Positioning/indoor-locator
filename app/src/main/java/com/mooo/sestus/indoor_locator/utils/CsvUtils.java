package com.mooo.sestus.indoor_locator.utils;

import android.graphics.PointF;
import android.os.AsyncTask;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtils {
    public static void writeToCsv(final String file, final PointF point) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(new File(file), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CSVWriter writer = new CSVWriter(fileWriter);
                writer.writeNext(String.format("%f,%f",point.x,point.y).split(","));
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static List<PointF> loadFloorPlanFromCsv(File file) {
        CSVReader reader = null;
        List<PointF> points = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String [] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                points.add(new PointF(Float.parseFloat(nextLine[0]), Float.parseFloat(nextLine[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static void saveFingerPrintsToCsv(final String file, final int pointId, final List<Float[]> values) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(new File(file), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CSVWriter writer = new CSVWriter(fileWriter);
                for (Float[] a : values)
                    writer.writeNext(String.format("%d,%f,%f,%f,%f,%f,%f",pointId,  a[0], a[1], a[2], a[3], a[4], a[5]).split(","));
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Map<Integer, List<Float[]>> loadFingerPrintsFromCsv(File file) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String [] nextLine;
        Map<Integer, List<Float[]>> fingerprints = new HashMap<>();
        try {
            while ((nextLine = reader.readNext()) != null) {
                int pointId = Integer.valueOf(nextLine[0]);
                List<Float[]> pointFingerPrints = fingerprints.get(pointId);
                if (pointFingerPrints == null) {
                    pointFingerPrints = new ArrayList<>();
                    fingerprints.put(pointId, pointFingerPrints);
                }
                Float[] measurements = new Float[6];
                for (int i = 1; i < nextLine.length; i++)
                    measurements[i-1] = (Float.valueOf(nextLine[i]));
                pointFingerPrints.add(measurements);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fingerprints;
    }


}
