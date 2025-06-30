package com.tdi;

import com.tdi.views.AppView;
import com.tdi.views.DialogView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.FileOutputStream;

import com.tdi.controllers.AppController;
import com.tdi.services.PriceUpdateService;
import com.tdi.services.CostUpdateService;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (FileNotFoundException e) {
            properties.setProperty("Duetz", "0,1,4,2,1");
            properties.setProperty("Hatz", "0,1,4,25%,4");
            properties.setProperty("Isuzu", "0,1,4,3,5");
            properties.setProperty("Perkins", "0,1,2,4,1");
            properties.setProperty("Vendors", "Duetz,Hatz,Isuzu,Perkins");
            properties.setProperty("QB_IMPORT_FILE_PATH", "./test_files");
            properties.setProperty("VENDOR_IMPORT_FILE_PATH", "./test_files");
            properties.setProperty("EXPORT_FILE_PATH", "./test_files");

            try {
                properties.store(new FileOutputStream("config.properties"), null);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AppView view = new AppView();
        DialogView dialogView = new DialogView(properties);
        PriceUpdateService priceUpdateService = new PriceUpdateService(properties);
        CostUpdateService costUpdateService = new CostUpdateService();
        new AppController(view, dialogView, priceUpdateService, costUpdateService, properties);
    }
}