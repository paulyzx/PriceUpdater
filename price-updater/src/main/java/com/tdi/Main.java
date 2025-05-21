package com.tdi;

import com.tdi.views.AppView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.FileOutputStream;

import com.tdi.controllers.AppController;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (FileNotFoundException e) {
            properties.setProperty("Perkins", "0,1,2,4");
            properties.setProperty("Duetz", "0,1,4,2");
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
        new AppController(view, properties);
    }
}