package com.tdi.models;

import java.util.ArrayList;
import java.util.List;

import com.tdi.models.config.VendorColumn;
import com.tdi.parsers.ExcelParser;

public class ExcelModel {
    private List<ImportModel> table = new ArrayList<ImportModel>();

    public ExcelModel(String filePath, VendorColumn vendorColumn) {
        table.clear();
        ExcelParser parser = new ExcelParser(filePath, vendorColumn);
        try {
            parser.Parse();
        } catch (Exception e) {
            System.out.println("Warning while parsing Excel file: " + e.getMessage());
        }
        parser.getParsedDataTable().forEach(row -> {
            if (row.size() == 4) {
                try {
                    table.add(new ImportModel(row.get(0).toString(), row.get(1).toString(), "", (Double) row.get(2),
                            (Double) row.get(3)));
                } catch (Exception e) {
                    System.out.println("Error adding row data: " + row.get(0) + " - " + e.getMessage());
                }
            } else {
                System.out.println("Error parsing Excel data: row size is not 4");
            }
        });
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        table.forEach(model -> sb.append(model.asString()).append("\n"));
        return sb.toString();
    }

    public List<ImportModel> getTable() {
        return table;
    }
}
