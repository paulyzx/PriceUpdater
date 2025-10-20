package com.tdi.parsers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tdi.models.config.VendorColumn;

public class ExcelParser {
    private String filePath;
    private ArrayList<ArrayList<Object>> parsedDataTable = new ArrayList<ArrayList<Object>>();
    private VendorColumn vendorColumn;

    /*
     * Constructor to initialize the ExcelParser with the file path and vendor
     * column configuration.
     * 
     * @param filePath The path to the Excel file to be parsed.
     * 
     * @param vendorColumn The configuration for the vendor columns, including
     * item number, description, list price, cost, and rows to
     * skip.
     */
    public ExcelParser(String filePath, VendorColumn vendorColumn) {
        this.filePath = filePath;
        this.vendorColumn = vendorColumn;
        System.out.println("Parsing Excel file: " + filePath);
    }

    public void Parse() {
        try (InputStream inp = (FileInputStream) new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inp);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();
            if (vendorColumn.rowsToSkip > 0) {
                for (int i = 0; i < vendorColumn.rowsToSkip; i++) {
                    if (iterator.hasNext()) {
                        iterator.next();
                    } else {
                        System.out.println(
                                "Warning: Not enough rows in the sheet to skip " + vendorColumn.rowsToSkip + " rows.");
                        return;
                    }
                }
            }

            Row row;
            while (iterator.hasNext()) {
                row = iterator.next();

                ArrayList<Object> rowList = new ArrayList<Object>();

                Object partNumber = parseCell(getCell(row, vendorColumn.itemNumber), evaluator, true);
                if (partNumber == null || partNumber.toString().isEmpty()) {
                    System.out.println("Skipping row with empty part number at row: " + row.getRowNum());
                    continue;
                }

                rowList.add(partNumber);
                rowList.add(parseCell(getCell(row, vendorColumn.description), evaluator, false));

                Object listPrice = parseCell(getCell(row, vendorColumn.listPrice), evaluator, false);
                rowList.add(listPrice);

                if (vendorColumn.isCostPercentage) {
                    double cost = (Double) listPrice * vendorColumn.costPercentage;
                    rowList.add(cost);
                } else {
                    rowList.add(parseCell(getCell(row, vendorColumn.cost), evaluator, false));
                }

                parsedDataTable.add(rowList);
            }

            System.out.println("Parsed " + parsedDataTable.size() + " rows");
        } catch (IOException xIo) {
            xIo.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> getParsedDataTable() {
        return parsedDataTable;
    }

    /**
     * Retrieves the cell at the specified column index from the given row.
     * If the cell is null or blank, it creates a new cell with an empty string.
     * 
     * @param row         The row from which to retrieve the cell.
     * @param columnIndex The index of the column.
     * @return The retrieved or newly created cell.
     */
    private Cell getCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            if (cell == null) {
                cell = row.createCell(columnIndex);
            }
            cell.setCellValue("");
            return cell;
        }
        return cell;
    }

    private Object parseCell(Cell cell, FormulaEvaluator evaluator, boolean isPartNumber) {
        Object value = null;
        try {
            switch (cell.getCellType()) {
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (isPartNumber) {
                        String partNumber = String.valueOf((int) cell.getNumericCellValue());
                        value = partNumber.replace("-", "");
                    } else {
                        value = cell.getNumericCellValue();
                    }
                    break;
                case FORMULA:
                    CellValue evaluatedValue = evaluator.evaluate(cell);
                    if (evaluatedValue.getCellType() == CellType.NUMERIC) {
                        value = evaluatedValue.getNumberValue();
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

        return value;
    }
}
