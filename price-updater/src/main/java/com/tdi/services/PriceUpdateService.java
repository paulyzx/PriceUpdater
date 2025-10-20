package com.tdi.services;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tdi.models.ExcelModel;
import com.tdi.models.ImportModel;
import com.tdi.models.config.Config;
import com.tdi.models.config.VendorColumn;
import com.tdi.parsers.QuickBooksDataParser;

public class PriceUpdateService {
    private static final ExecutorService IO_EXECUTOR = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            IO_EXECUTOR.shutdown();
            try {
                if (!IO_EXECUTOR.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                    IO_EXECUTOR.shutdownNow();
                }
            } catch (InterruptedException e) {
                IO_EXECUTOR.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));
    }
    private Config config;
    private QuickBooksDataParser quickBooksDataParser;
    private final Map<String, ExcelModel> excelModelCache = new ConcurrentHashMap<>();

    public PriceUpdateService(Properties properties) {
        this.config = new Config(properties);
    }

    /**
     * Loads and parses QuickBooks data from the specified file.
     * Note: This data is currently in the from of a CSV file exported from
     * QuickBooks.
     * 
     * @param file The QuickBooks data file.
     * @return A string representation of the QuickBooks data.
     */
    public String getQuickBooksData(File file) {
        String filePath = file.getAbsolutePath();
        quickBooksDataParser = new QuickBooksDataParser(filePath);
        quickBooksDataParser.create();
        return quickBooksDataParser.getMapAsString();
    }

    /**
     * Loads and parses Excel data from the specified vendor file for the given
     * vendor.
     * 
     * @param file       The Excel data file.
     * @param vendorName The name of the vendor.
     * @return A string representation of the Excel data.
     */
    public CompletableFuture<String> getExcelData(File file, String vendorName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String filePath = file.getAbsolutePath();
                VendorColumn vendorColumn = config.vendorMap.get(vendorName);
                ExcelModel model = new ExcelModel(filePath, vendorColumn);
                excelModelCache.put(vendorName, model);
                return model.getAsString();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load Excel data for vendor: " + vendorName, e);
            }
        }, IO_EXECUTOR);
    }

    public Boolean isSelectedVendorValid(String selectedVendor) {
        return config.vendorMap.containsKey(selectedVendor);
    }

    public Boolean vendorHasConfiguration(String selectedVendor) {
        VendorColumn vendorColumn = config.vendorMap.get(selectedVendor);
        return vendorColumn != null;
    }

    public String getVendorColumnsAsString(String vendorName) {
        return config.vendorMap.get(vendorName).getAsString();
    }

    /**
     * Gets the QuickBooks data as a map. Used for displaying data in the UI.
     * 
     * @return A HashMap representing the QuickBooks data.
     */
    public HashMap<String, ImportModel> getQbMap() {
        return quickBooksDataParser.getQbMap();
    }

    public List<ImportModel> getExcelTable(String vendorName) {
        ExcelModel model = excelModelCache.get(vendorName);
        if (model == null) {
            throw new IllegalStateException("No Excel data loaded for vendor: " + vendorName);
        }
        return model.getTable();
    }
}
