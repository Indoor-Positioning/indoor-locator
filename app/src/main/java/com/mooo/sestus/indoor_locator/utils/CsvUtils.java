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
import java.util.List;

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

    public static List<PointF> readCsv(File file) {
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
}
