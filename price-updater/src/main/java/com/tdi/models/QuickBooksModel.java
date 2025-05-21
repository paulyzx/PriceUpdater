package com.tdi.models;

public class QuickBooksModel {
    public String ActiveStatus;
    public String Type;
    public String Item;
    public String Description;
    public String SalesTaxCode;
    public String Account;
    public String COGSAccount;
    public String AssetAccount;
    public double AccumulatedDepreciation;
    public String PurchaseDescription;
    public int QuantityOnHand;
    public double Cost;
    public String PreferredVendor;
    public String TaxAgency;
    public double Price;
    public int ReorderPtMin;
    public String MPN;
    public String Location;

    public QuickBooksModel(String ActiveStatus, String Type, String Item, String Description, String SalesTaxCode,
            String Account, String COGSAccount, String AssetAccount, double AccumulatedDepreciation,
            String PurchaseDescription, int QuantityOnHand, double Cost, String PreferredVendor, String TaxAgency,
            double Price, int ReorderPtMin, String MPN, String Location) {
        this.ActiveStatus = ActiveStatus;
        this.Type = Type;
        this.Item = Item;
        this.Description = Description;
        this.SalesTaxCode = SalesTaxCode;
        this.Account = Account;
        this.COGSAccount = COGSAccount;
        this.AssetAccount = AssetAccount;
        this.AccumulatedDepreciation = AccumulatedDepreciation;
        this.PurchaseDescription = PurchaseDescription;
        this.QuantityOnHand = QuantityOnHand;
        this.Cost = Cost;
        this.PreferredVendor = PreferredVendor;
        this.TaxAgency = TaxAgency;
        this.Price = Price;
        this.ReorderPtMin = ReorderPtMin;
        this.MPN = MPN;
        this.Location = Location;
    }
}