package com.tdi.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tdi.models.ImportModel;
import com.tdi.models.QuickBooksModel;

/**
 * Parser class for QuickBooks CSV data files.
 */
public class QuickBooksDataParser {
    private List<QuickBooksModel> quickBooksData;
    private List<ImportModel> table = new ArrayList<ImportModel>();
    private HashMap<String, ImportModel> qbMap = new HashMap<String, ImportModel>();
    private HashMap<String, String> duplicatePartNumberHashMap = new HashMap<String, String>();
    private String filePath;
    private static final String PART_TYPE = "Inventory Part";

    public QuickBooksDataParser(String filePath) {
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

        if (duplicatePartNumberHashMap.size() > 0) {
            System.out.println("Duplicate part numbers found in QuickBooks data:");
            int index = 1;
            for (String value : duplicatePartNumberHashMap.values()) {
                System.out.println(index++ + ": " + value);
            }
        }
    }

    private void addImportMap(String[] row) {
        try {
            ImportModel importModel = new ImportModel(row[3], row[4], row[2], parseDouble(row[15]),
                    parseDouble(row[12]));

            if (importModel.getType().equals(PART_TYPE)) {
                String searchablePartNumber = importModel.getSearchablePartNumber();
                if (qbMap.containsKey(searchablePartNumber)) {
                    duplicatePartNumberHashMap.put(searchablePartNumber,
                            importModel.getPartNumber() + " " + importModel.getDescription());
                }
                qbMap.put(searchablePartNumber, importModel);
            }
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
            if (importModel.getType().equals(PART_TYPE)) {
                table.add(importModel);
            }
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
            if (quickBooksModel.getType().equals(PART_TYPE))
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
            sb.append(quickBooksModel.getActiveStatus()).append(",");
            sb.append(quickBooksModel.getType()).append(",");
            sb.append(quickBooksModel.getItem()).append(",");
            sb.append(quickBooksModel.getSearchableItem()).append(",");
            sb.append(quickBooksModel.getDescription()).append(",");
            sb.append(quickBooksModel.getSalesTaxCode()).append(",");
            sb.append(quickBooksModel.getAccount()).append(",");
            sb.append(quickBooksModel.getCOGSAccount()).append(",");
            sb.append(quickBooksModel.getAssetAccount()).append(",");
            sb.append(quickBooksModel.getAccumulatedDepreciation()).append(",");
            sb.append(quickBooksModel.getPurchaseDescription()).append(",");
            sb.append(quickBooksModel.getQuantityOnHand()).append(",");
            sb.append(quickBooksModel.getCost()).append(",");
            sb.append(quickBooksModel.getPreferredVendor()).append(",");
            sb.append(quickBooksModel.getTaxAgency()).append(",");
            sb.append(quickBooksModel.getPrice()).append(",");
            sb.append(quickBooksModel.getReorderPtMin()).append(",");
            sb.append(quickBooksModel.getMPN()).append(",");
            sb.append(quickBooksModel.getLocation()).append("\n");
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
