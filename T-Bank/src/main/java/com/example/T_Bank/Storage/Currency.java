package com.example.T_Bank.Storage;

public class Currency {
    String currencyName;
    String currencyId;
    private double call;
    private double bid;


    public Currency(String currencyName, String currencyId, double call, double bid) {
        this.currencyName = currencyName;
        this.currencyId = currencyId;
        this.call = call;
        this.bid = bid;
    }

    public double getCall() {
        return call;
    }

    public double getBid() {
        return bid;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyId() {
        return currencyId;
    }
}
