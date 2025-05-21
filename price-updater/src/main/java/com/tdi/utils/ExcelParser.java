package com.tdi.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelParser {
    private String filePath;
    private ArrayList<ArrayList<Object>> table = new ArrayList<ArrayList<Object>>();
    private List<Integer> columnKeys;
    private int rowsToSkip;

    /**
     * Constructs an ExcelParser with the specified file path and column keys.
     *
     * @param filePath   the path to the Excel file to be parsed
     * @param columnKeys the list of required column indices with an order of
     *                   partNumber,
     *                   description, listPrice, and cost.
     */
    public ExcelParser(String filePath, List<Integer> columnKeys, int rowsToSkip) {
        this.filePath = filePath;
        this.columnKeys = columnKeys;
        this.rowsToSkip = rowsToSkip;

        System.out.println("Parsing Excel file: " + filePath);
    }

    /**
     * 
     */
    public void Parse() {
        try (InputStream inp = (FileInputStream) new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inp);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowsToSkip);

            Iterator<Row> iterator = sheet.iterator();
            if (iterator.hasNext()) {
                iterator.next();
                iterator.next();
                iterator.next();
                iterator.next();
            }

            while (iterator.hasNext()) {
                row = iterator.next();

                ArrayList<Object> rowList = new ArrayList<Object>();
                for (int i = 0; i < columnKeys.size(); i++) {
                    Cell cell = row.getCell(columnKeys.get(i));
                    if (cell == null) {
                        continue;
                    }
                    parseCell(cell, rowList, evaluator, i == 0);
                }
                table.add(rowList);
            }

            System.out.println("Parsed " + table.size() + " rows");
        } catch (IOException xIo) {
            xIo.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> getTable() {
        return table;
    }

    private void parseCell(Cell cell, ArrayList<Object> rowList, FormulaEvaluator evaluator, boolean isPartNumber) {
        try {
            switch (cell.getCellType()) {
                case STRING:
                    rowList.add(cell.getStringCellValue());
                    break;
                case NUMERIC:
                    if (isPartNumber) {
                        String partNumber = String.valueOf((int) cell.getNumericCellValue());
                        rowList.add(partNumber.replace("-", ""));
                    } else {
                        rowList.add(cell.getNumericCellValue());
                    }
                    break;
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        rowList.add(evaluatedValue.getNumberValue());
                    } else {
                        System.out.println("Error parsing Excel data: formula cell is not numeric");
                    }
                    break;
                case BLANK:
                    System.out.println("Blank cell");
                    break;
                default:
                    System.out.println("Error parsing Excel data: unknown cell type");
            }
        } catch (Exception e) {
            System.out.println("Error parsing Excel data: " + e.getMessage());
        }
    }
}
