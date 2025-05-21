package com.tdi.models.config;

public class Vendor {
    public int itemNumber;
    public int description;
    public int listPrice;
    public int cost;

    public Vendor(int itemNumber, int description, int listPrice, int cost) {
        this.itemNumber = itemNumber;
        this.description = description;
        this.listPrice = listPrice;
        this.cost = cost;
    }
}
