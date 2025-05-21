package com.tdi.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tdi.utils.ExcelParser;

public class ExcelModel {
    private List<ImportModel> table = new ArrayList<ImportModel>();

    public ExcelModel(String filePath, String columnList) {
        table.clear();
        List<Integer> columns = Stream.of(columnList.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        int rowsToSkip = columns.get(columns.size() - 1);
        columns.remove(columns.size() - 1);

        ExcelParser parser = new ExcelParser(filePath, columns, rowsToSkip);
        try {
            parser.Parse();
        } catch (Exception e) {
            System.out.println("Error parsing Excel file: " + e.getMessage());
        }
        parser.getTable().forEach(row -> {
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
