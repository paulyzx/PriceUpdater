package com.tdi.controllers;

import com.tdi.views.AppView;
import com.tdi.views.DialogView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Optional;
import java.util.Properties;

import javax.swing.SwingUtilities;

import com.tdi.services.CostUpdateService;
import com.tdi.services.PriceUpdateService;

public class AppController {
    private AppView view;
    private DialogView dialogView;
    private PriceUpdateService priceUpdateService;
    private CostUpdateService costUpdateService;
    private String selectedVendor;

    public AppController(AppView view,
            DialogView dialogView,
            PriceUpdateService priceUpdateService,
            CostUpdateService costUpdaterService,
            Properties properties) {
        this.view = view;
        this.dialogView = dialogView;
        this.priceUpdateService = priceUpdateService;
        this.costUpdateService = costUpdaterService;
        this.view.addButtonListener(new LoadCsvListener());
        this.view.addPerkinsListener(new GetVendorListener());
        this.view.addCreateExportCsvListener(new CreateExportCsvListener());
        this.view.addSaveCsvListener(new SaveCsvListener());
    }

    class LoadCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.displayData("Loading QuickBooks CSV...");
            File file = dialogView.showLoadCsvFileChooser();
            if (file != null) {
                view.displayData(priceUpdateService.getQuickBooksData(file));
            } else {
                view.displayData("");
            }
        }
    }

    class GetVendorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.displayImportData("Selecting vendor...");

            selectedVendor = dialogView.showInputDialog("Select vendor", "Vendor");

            if (selectedVendor == null) {
                System.out.println("User cancelled");
                view.displayImportData("");
                return;
            }

            if (!priceUpdateService.isSelectedVendorValid(selectedVendor)) {
                System.out.println("Invalid vendor selected: " + selectedVendor);
                dialogView.showMessageDialog("Invalid vendor selected: " + selectedVendor, "Error");
                return;
            }

            if (!priceUpdateService.vendorHasConfiguration(selectedVendor)) {
                System.out.println("No configuration found for vendor: " + selectedVendor);
                dialogView.showMessageDialog("No configuration found for vendor: " + selectedVendor, "Error");
                return;
            }

            System.out.println("Selected vendor: " + selectedVendor + " with columns: "
                    + priceUpdateService.getVendorColumnsAsString(selectedVendor));

            File file = dialogView.showVendorFileChooser();
            if (file != null) {
                view.displayImportData("Processing vendor file: " + file.getName());
                priceUpdateService.getExcelData(file, selectedVendor)
                        .thenAccept(data -> SwingUtilities.invokeLater(() -> view.displayImportData(data)));
            } else {
                view.displayImportData("");
            }
        }
    }

    class CreateExportCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.displayExportData("Generating export CSV...");
            try {
                Optional<String> exportData = costUpdateService.createExportCsv(priceUpdateService.getQbMap(),
                        priceUpdateService.getExcelTable(selectedVendor));
                view.displayExportData(exportData.orElse("No export data generated."));
            } catch (Exception ex) {
                view.displayExportData("Error generating export CSV: " + ex.getMessage());
                return;
            }
        }
    }

    class SaveCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File fileToSave = dialogView.showExportFileChooser();
            if (fileToSave != null) {
                costUpdateService.saveToFile(fileToSave);
                dialogView.showMessageDialog("File saved successfully!", "Success");
            }
        }
    }
}