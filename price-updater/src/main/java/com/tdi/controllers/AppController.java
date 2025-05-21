package com.tdi.controllers;

import javax.swing.*;

import com.tdi.views.AppView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import com.tdi.models.ExcelModel;
import com.tdi.utils.CostUpdater;
import com.tdi.utils.QuickBooksData;

public class AppController {
    private AppView view;
    private ExcelModel excelModel;
    private QuickBooksData quickBooksData;
    private CostUpdater costUpdater;
    private Properties properties;
    private String selectedVendor;

    public AppController(AppView view, Properties properties) {
        this.view = view;
        this.properties = properties;
        this.view.addButtonListener(new LoadCsvListener());
        this.view.addPerkinsListener(new GetVendorListener());
        this.view.addCreateExportCsvListener(new CreateExportCsvListener());
        this.view.addSaveCsvListener(new SaveCsvListener());
    }

    class LoadCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser fileChooser = new JFileChooser(
                    new File("./test_files"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                quickBooksData = new QuickBooksData(filePath);
                quickBooksData.create();
                view.displayData(quickBooksData.getMapAsString());
            }
        }
    }

    class GetVendorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = new JFrame();
            frame.setAlwaysOnTop(true);
            Object[] options = properties.getProperty("Options").split(",");
            selectedVendor = (String) JOptionPane.showInputDialog(frame, "Select vendor", "Vendor",
                    JOptionPane.QUESTION_MESSAGE, null, options, null);

            if (selectedVendor == null) {
                System.out.println("User cancelled");
                return;
            }

            String columnsToParse = properties.getProperty(selectedVendor);
            System.out.println("Selected vendor: " + selectedVendor + " with columns: " + columnsToParse);

            String vendorImportFilePath = properties.getProperty("VENDOR_IMPORT_FILE_PATH");
            JFileChooser fileChooser = new JFileChooser(
                    new File(System.getProperty("user.dir"), vendorImportFilePath));
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                excelModel = new ExcelModel(filePath, columnsToParse);
                view.displayImportData(excelModel.getAsString());
            }
        }
    }

    class CreateExportCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            costUpdater = new CostUpdater();
            costUpdater.updateTable(quickBooksData.getQbMap(), excelModel.getTable());
            view.displayExportData(costUpdater.getAsString());
        }
    }

    class SaveCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String fileChooserFilePath = properties.getProperty("VENDOR_IMPORT_FILE_PATH");
            JFileChooser fileChooser = new JFileChooser(
                    new File(fileChooserFilePath));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());

                costUpdater.saveToFile(fileToSave.getAbsolutePath());

                JOptionPane.showMessageDialog(null, "File saved successfully!");
            }
        }
    }
}