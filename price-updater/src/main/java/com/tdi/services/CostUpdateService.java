package com.tdi.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.tdi.models.ImportModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CostUpdateService {
    private List<ImportModel> newTable = new ArrayList<ImportModel>();

    public CostUpdateService() {
    }

    public Optional<String> createExportCsv(HashMap<String, ImportModel> qbMab, List<ImportModel> vendorImportedData) {
        if (qbMab == null || vendorImportedData == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        try {
            updateExportTable(qbMab, vendorImportedData);
            return Optional.of(getAsString());
        } catch (Exception e) {
            Logger logger = Logger.getLogger("TdLogger");
            logger.severe("Error creating export CSV: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Updates the table to be exported with new cost and price information based on
     * the QuickBooks data and vendor imported data.
     * 
     * @param qbMap              A map of QuickBooks data keyed by searchable part
     *                           number.
     * @param vendorImportedData A list of imported data from the vendor.
     */
    private void updateExportTable(HashMap<String, ImportModel> qbMap, List<ImportModel> vendorImportedData) {
        newTable.clear();
        int foundCount = 0;
        int priceChanged = 0;
        int priceNotChanged = 0;
        int notFoundCount = 0;
        for (ImportModel vendorImportModel : vendorImportedData) {
            ImportModel qbrow = qbMap.get(vendorImportModel.getSearchablePartNumber());
            if (qbrow != null) {
                if (hasPriceChanged(qbrow, vendorImportModel)) {
                    ImportModel newRow = new ImportModel(
                            qbrow.getPartNumber(),
                            qbrow.getDescription(),
                            qbrow.getType(),
                            vendorImportModel.getListPrice(),
                            vendorImportModel.getCost());
                    newTable.add(newRow);
                    ++priceChanged;
                } else {
                    ++priceNotChanged;
                }
                ++foundCount;
            } else {
                ++notFoundCount;
            }
        }

        Logger logger = Logger.getLogger("TdLogger");
        logger.info("Found: " + foundCount + " items, didn't find: " + notFoundCount + " items. Price changed: "
                + priceChanged + " items, price not changed: " + priceNotChanged + " items.");
    }

    private boolean hasPriceChanged(ImportModel qbModel, ImportModel excModel) {
        return Math.round(qbModel.getCost() * 100.0) != Math.round(excModel.getCost() * 100.0)
                || Math.round(qbModel.getListPrice() * 100.0) != Math.round(excModel.getListPrice() * 100.0);
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        newTable.forEach(model -> sb.append(model.asString()).append("\n"));
        return sb.toString();
    }

    public void saveToFile(File file) {
        String filePath = file.getAbsolutePath();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Item,Cost,Price,Type");
            writer.newLine();
            for (ImportModel model : newTable) {
                writer.write(model.asString());
                writer.newLine();
            }
        } catch (IOException e) {
            Logger logger = Logger.getLogger("TdLogger");
            logger.severe("Error writing to file: " + e.getMessage());
        }

        System.out.println("File saved as: " + filePath);
    }
}
