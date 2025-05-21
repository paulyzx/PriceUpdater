package com.tdi.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private List<String[]> data;
    private String filePath;
    final String DELIMITER = ",";
    final String TARGET = "-";
    final String REPLACEMENT = "";
    final String END_OF_LINE = "\n";

    public CsvParser(String filePath) {
        this.filePath = filePath;
        data = new ArrayList<>();
        System.out.println("Parsing Excel file: " + filePath);
    }

    public void parseCsv() {
        /*
         * Example of a CSV file:
         * ,"Active","Inventory Part","1109048","Brg","Tax","Parts Sales","Cost of Goods
         * Sold","Inventory Asset",0.00,"Brg",2,24.50,"Engine
         * Distributors,Inc..",,32.67,"",,
         */
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = csvSplit(line);
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] csvSplit(String line) {
        int quoteCount = 0;
        String value = "";
        List<String> values = new ArrayList<>();
        for (char c : line.toCharArray()) {
            if (c == ',' && quoteCount % 2 == 0) {
                values.add(value.replace(TARGET, REPLACEMENT));
                value = "";
            } else if (c == '"') {
                quoteCount++;
            } else {
                value += c;
            }
        }
        values.add(value);
        return values.toArray(new String[values.size()]);
    }

    public List<String[]> getData() {
        return data;
    }

    public String getDataAsString() {
        StringBuilder sb = new StringBuilder();
        for (String[] row : data) {
            sb.append(String.join(DELIMITER, row)).append(END_OF_LINE);
        }
        return sb.toString();
    }
}
