package com.tdi.models;

public class ImportModel {
    private String partNumber;
    private String description;
    private String type;
    private double listPrice;
    private double cost;

    public ImportModel(String partNumber, String description, String Type, double listPrice, double cost) {
        this.partNumber = partNumber;
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
