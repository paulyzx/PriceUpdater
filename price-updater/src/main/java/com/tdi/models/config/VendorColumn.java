package com.tdi.models.config;

/**
 * Model class representing vendor column configuration.
 */
public class VendorColumn {
    public int itemNumber;
    public int description;
    public int listPrice;
    public int cost;
    public int rowsToSkip;
    public boolean isCostPercentage = false;
    public double costPercentage = 0.0;

    public VendorColumn(String configProperty) {
        String[] parts = configProperty.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid config property: " + configProperty);
        }

        this.itemNumber = Integer.parseInt(parts[0]);
        this.description = Integer.parseInt(parts[1]);
        this.listPrice = Integer.parseInt(parts[2]);
        this.rowsToSkip = Integer.parseInt(parts[4]);

        String costValue = parts[3];
        if (costValue.endsWith("%")) {
            this.isCostPercentage = true;
            // Input percentage represents margin/discount; convert to actual cost
            // percentage, e.g., "25%" margin → 75% of list price becomes the actual cost
            double parsedPercentage = Double.parseDouble(costValue.replace("%", ""));
            if (parsedPercentage < 0 || parsedPercentage > 100) {
                throw new IllegalArgumentException(
                        "Cost percentage must be between 0 and 100, got: " + parsedPercentage);
            }
            this.costPercentage = (100 - parsedPercentage) / 100.0;
        } else {
            this.isCostPercentage = false;
            this.cost = Integer.parseInt(costValue);
        }
    }

    public String getAsString() {
        String costValue = isCostPercentage ? String.format("%.1f%%", costPercentage * 100) : String.valueOf(cost);
        return itemNumber + "," + description + "," + listPrice + "," + costValue + "," + rowsToSkip;
    }
}
