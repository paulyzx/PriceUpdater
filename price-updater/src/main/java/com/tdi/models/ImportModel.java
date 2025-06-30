package com.tdi.models;

public class ImportModel {
    private String partNumber;
    private String description;
    private String type;
    private double listPrice;
    private double cost;
    private String searchPartNumber;

    private static final String TARGET = "-";
    private static final String REPLACEMENT = "";

    public ImportModel(String partNumber, String description, String Type, double listPrice, double cost) {
        this.partNumber = partNumber;
        this.searchPartNumber = partNumber != null ? partNumber.replace(TARGET, REPLACEMENT) : "";
        this.description = description;
        this.listPrice = listPrice;
        this.cost = cost;
        this.type = Type;
    }

    public String asString() {
        return String.format("%s,%.2f,%.2f,%s", partNumber, cost, listPrice, type);
    }

    public String getPartNumber() {
        return partNumber;
    }

    public String getSearchablePartNumber() {
        return searchPartNumber;
    }

    public double getCost() {
        return cost;
    }

    public double getListPrice() {
        return listPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
