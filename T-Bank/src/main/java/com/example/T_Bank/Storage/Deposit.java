package com.example.T_Bank.Storage;

import java.sql.Timestamp;

public class Deposit {
    private int depositID;
    private String depositName;
    private int accountId;
    private String cardIdentifier;
    private double balance;
    private int currencyId;
    private double percent;
    private int periods;
    private Timestamp startDate;
    private Timestamp lastUpdate;
    private Timestamp endDate;
    private boolean active;
    private double startMoney;

    public Deposit(int depositID, String depositName, int accountId, String cardIdentifier,
                   double balance, int currencyId, double percent, int periods, Timestamp startDate,
                   Timestamp lastUpdate, Timestamp endDate, boolean active, double aDouble) {
        this.depositID = depositID;
        this.depositName = depositName;
        this.accountId = accountId;
        this.cardIdentifier = cardIdentifier;
        this.balance = balance;
        this.currencyId = currencyId;
        this.percent = percent;
        this.periods = periods;
        this.startDate = startDate;
        this.lastUpdate = lastUpdate;
        this.endDate = endDate;
        this.active = active;
        this.startMoney = startMoney;
    }

    public double getStartMoney() {
        return startMoney;
    }

    public int getDepositID() {
        return depositID;
    }

    public void setDepositID(int depositID) {
        this.depositID = depositID;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public void setCardIdentifier(String cardIdentifier) {
        this.cardIdentifier = cardIdentifier;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
