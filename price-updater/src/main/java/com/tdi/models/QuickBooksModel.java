package com.tdi.models;

public class QuickBooksModel {
    private String activeStatus;
    private String type;
    private String item;
    private String searchableItem;
    private String description;
    private String salesTaxCode;
    private String account;
    private String cogsAccount;
    private String assetAccount;
    private double accumulatedDepreciation;
    private String purchaseDescription;
    private int quantityOnHand;
    private double cost;
    private String preferredVendor;
    private String taxAgency;
    private double price;
    private int reorderPtMin;
    private String mpn;
    private String location;

    private static final String TARGET = "-";
    private static final String REPLACEMENT = "";

    public QuickBooksModel(String ActiveStatus, String Type, String Item, String Description, String SalesTaxCode,
            String Account, String COGSAccount, String AssetAccount, double AccumulatedDepreciation,
            String PurchaseDescription, int QuantityOnHand, double Cost, String PreferredVendor, String TaxAgency,
            double Price, int ReorderPtMin, String MPN, String Location) {
        this.activeStatus = ActiveStatus;
        this.type = Type;
        this.item = Item;
        this.searchableItem = this.item != null ? this.item.replace(TARGET, REPLACEMENT) : "";
        this.description = Description;
        this.salesTaxCode = SalesTaxCode;
        this.account = Account;
        this.cogsAccount = COGSAccount;
        this.assetAccount = AssetAccount;
        this.accumulatedDepreciation = AccumulatedDepreciation;
        this.purchaseDescription = PurchaseDescription;
        this.quantityOnHand = QuantityOnHand;
        this.cost = Cost;
        this.preferredVendor = PreferredVendor;
        this.taxAgency = TaxAgency;
        this.price = Price;
        this.reorderPtMin = ReorderPtMin;
        this.mpn = MPN;
        this.location = Location;
    }

    // Getters and Setters
    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
        this.searchableItem = item != null ? item.replace(TARGET, REPLACEMENT) : "";
    }

    public String getSearchableItem() {
        return searchableItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalesTaxCode() {
        return salesTaxCode;
    }

    public void setSalesTaxCode(String salesTaxCode) {
        this.salesTaxCode = salesTaxCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCOGSAccount() {
        return cogsAccount;
    }

    public void setCOGSAccount(String cogsAccount) {
        this.cogsAccount = cogsAccount;
    }

    public String getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(String assetAccount) {
        this.assetAccount = assetAccount;
    }

    public double getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public void setAccumulatedDepreciation(double accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public String getPurchaseDescription() {
        return purchaseDescription;
    }

    public void setPurchaseDescription(String purchaseDescription) {
        this.purchaseDescription = purchaseDescription;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getPreferredVendor() {
        return preferredVendor;
    }

    public void setPreferredVendor(String preferredVendor) {
        this.preferredVendor = preferredVendor;
    }

    public String getTaxAgency() {
        return taxAgency;
    }

    public void setTaxAgency(String taxAgency) {
        this.taxAgency = taxAgency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReorderPtMin() {
        return reorderPtMin;
    }

    public void setReorderPtMin(int reorderPtMin) {
        this.reorderPtMin = reorderPtMin;
    }

    public String getMPN() {
        return mpn;
    }

    public void setMPN(String mpn) {
        this.mpn = mpn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}