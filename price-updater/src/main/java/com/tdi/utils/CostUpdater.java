package com.tdi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.tdi.models.ImportModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CostUpdater {
    private List<ImportModel> newTable = new ArrayList<ImportModel>();

    public CostUpdater() {
    }

    public void updateTable(HashMap<String, ImportModel> qbMap, List<ImportModel> excelTable) {
        newTable.clear();
        int foundCount = 0;
        int priceChanged = 0;
        int priceNotChanged = 0;
        int notFoundCount = 0;
        for (ImportModel excelModel : excelTable) {
            ImportModel qbrow = qbMap.get(excelModel.getPartNumber());
            if (qbrow != null) {
                if (hasPriceChanged(qbrow, excelModel)) {
                    ImportModel newRow = new ImportModel(qbrow.getPartNumber(), qbrow.getDescription(), qbrow.getType(),
                            excelModel.getListPrice(), excelModel.getCost());
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

        Logger logger = Logger.getLogger("MyLogger");
        logger.info("Found: " + foundCount + " items, didn't find: " + notFoundCount + " items. Price changed: "
                + priceChanged + " items, price not changed: " + priceNotChanged + " items.");
    }

    private boolean hasPriceChanged(ImportModel qbModel, ImportModel excModel) {
        if (qbModel.getPartNumber() == "1109048")
            System.out.println("Cost: " + qbModel.getCost() + " " + excModel.getCost());
        return Math.round(qbModel.getCost() * 100.0) != Math.round(excModel.getCost() * 100.0)
                || Math.round(qbModel.getListPrice() * 100.0) != Math.round(excModel.getListPrice() * 100.0);
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        newTable.forEach(model -> sb.append(model.asString()).append("\n"));
        return sb.toString();
    }

    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Item,Cost,Price,Type");
            writer.newLine();
            for (ImportModel model : newTable) {
                writer.write(model.asString());
                writer.newLine();
            }
        } catch (IOException e) {
            Logger logger = Logger.getLogger("MyLogger");
            logger.severe("Error writing to file: " + e.getMessage());
        }
    }
}
