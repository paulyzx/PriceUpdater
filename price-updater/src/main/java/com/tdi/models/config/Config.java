package com.tdi.models.config;

import java.util.HashMap;
import java.util.Properties;

/**
 * Configuration model class that holds vendor configurations and file paths.
 */
public class Config {
    public HashMap<String, VendorColumn> vendorMap = new HashMap<String, VendorColumn>();
    public Object[] Options = null;
    public String QbImportFilePath = "";
    public String VendorImportFilePath = "";
    public String ExportFilePath = "";

    public Config(Properties properties) {
        Options = properties.getProperty("Vendors").split(",");
        for (Object option : Options) {
            String vendorName = option.toString();
            String configProperty = properties.getProperty(vendorName);
            if (configProperty != null) {
                VendorColumn vendorColumn = new VendorColumn(configProperty);
                vendorMap.put(vendorName, vendorColumn);
            } else {
                System.out.println("No configuration found for vendor: " + vendorName);
            }
        }

        QbImportFilePath = properties.getProperty("QB_IMPORT_FILE_PATH", "./test_files");
        VendorImportFilePath = properties.getProperty("VENDOR_IMPORT_FILE_PATH", "./test_files");
        ExportFilePath = properties.getProperty("EXPORT_FILE_PATH", "./test_files");
    }
}
