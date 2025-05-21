package com.tdi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tdi.models.ImportModel;
import com.tdi.models.QuickBooksModel;

public class QuickBooksData {
    private List<QuickBooksModel> quickBooksData;
    private List<ImportModel> table = new ArrayList<ImportModel>();
    private HashMap<String, ImportModel> qbMap = new HashMap<String, ImportModel>();
    private String filePath;

    public QuickBooksData(String filePath) {
        this.filePath = filePath;
        this.quickBooksData = new ArrayList<QuickBooksModel>();
    }

    public void create() {
        CsvParser parser = new CsvParser(filePath);
        parser.parseCsv();

        quickBooksData.clear();
        parser.getData().stream().skip(1).forEach(this::add);
        parser.getData().stream().skip(1).forEach(this::addImportModel);
        parser.getData().stream().skip(1).forEach(this::addImportMap);
    }

    private void addImportMap(String[] row) {
        try {
            ImportModel importModel = new ImportModel(row[3], row[4], row[2], parseDouble(row[15]),
                    parseDouble(row[12]));
            qbMap.put(importModel.getPartNumber(), importModel);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Import data: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error parsing Import data: " + e.getMessage());
        }
    }

    private void addImportModel(String[] row) {
        try {
            ImportModel importModel = new ImportModel(row[3], row[4], row[2], parseDouble(row[15]),
                    parseDouble(row[12]));
            table.add(importModel);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Import data: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error parsing Import data: " + e.getMessage());
        }
    }

    private void add(String[] row) {
        try {
            QuickBooksModel quickBooksModel = new QuickBooksModel(row[1], row[2], row[3], row[4], row[5], row[6],
                    row[7], row[8], parseDouble(row[9]), row[10], parseInt(row[11]), parseDouble(row[12]),
                    row[13], row[14], parseDouble(row[15]), parseInt(row[16]), row[17], row[18]);
            quickBooksData.add(quickBooksModel);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing QuickBooks data: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error parsing QuickBooks data: " + e.getMessage());
        }
    }

    public List<QuickBooksModel> get() {
        return quickBooksData;
    }

    public HashMap<String, ImportModel> getQbMap() {
        return qbMap;
    }

    public String getMapAsString() {
        StringBuilder sb = new StringBuilder();
        for (String key : qbMap.keySet()) {
            sb.append(qbMap.get(key).asString()).append("\n");
        }
        return sb.toString();
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        for (ImportModel importModel : table) {
            sb.append(importModel.asString()).append("\n");
        }
        return sb.toString();
    }

    public String getAllAsString() {
        StringBuilder sb = new StringBuilder();
        for (QuickBooksModel quickBooksModel : quickBooksData) {
            sb.append(quickBooksModel.ActiveStatus).append(",");
            sb.append(quickBooksModel.Type).append(",");
            sb.append(quickBooksModel.Item).append(",");
            sb.append(quickBooksModel.Description).append(",");
            sb.append(quickBooksModel.SalesTaxCode).append(",");
            sb.append(quickBooksModel.Account).append(",");
            sb.append(quickBooksModel.COGSAccount).append(",");
            sb.append(quickBooksModel.AssetAccount).append(",");
            sb.append(quickBooksModel.AccumulatedDepreciation).append(",");
            sb.append(quickBooksModel.PurchaseDescription).append(",");
            sb.append(quickBooksModel.QuantityOnHand).append(",");
            sb.append(quickBooksModel.Cost).append(",");
            sb.append(quickBooksModel.PreferredVendor).append(",");
            sb.append(quickBooksModel.TaxAgency).append(",");
            sb.append(quickBooksModel.Price).append(",");
            sb.append(quickBooksModel.ReorderPtMin).append(",");
            sb.append(quickBooksModel.MPN).append(",");
            sb.append(quickBooksModel.Location).append("\n");
        }
        return sb.toString();
    }

    private double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
