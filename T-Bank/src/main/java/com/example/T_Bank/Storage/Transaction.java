package com.example.T_Bank.Storage;

import java.util.Date;

public class Transaction {
    private int senderAccountId;
    private int receiverAccountId;
    private String senderCardIdentifier;
    private String receiverCardIdentifier;
    private int transactionTypeId;
    private Date logDate;
    private int currencyId;
    private double amount;

    public Transaction(int senderAccountId, int receiverAccountId, String senderCardIdentifier, String receiverCardIdentifier, int transactionTypeId, Date date,int currencyId,double amount) {
        this.senderAccountId=senderAccountId;
        this.receiverAccountId=receiverAccountId;
        this.senderCardIdentifier=senderCardIdentifier;
        this.receiverCardIdentifier=receiverCardIdentifier;
        this.transactionTypeId=transactionTypeId;
        logDate=date;
        this.amount=amount;
        this.currencyId=currencyId;

    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderAccountId=" + senderAccountId +
                ", receiverAccountId=" + receiverAccountId +
                ", senderCardIdentifier='" + senderCardIdentifier + '\'' +
                ", receiverCardIdentifier='" + receiverCardIdentifier + '\'' +
                ", transactionTypeId=" + transactionTypeId +
                ", logDate=" + logDate +
                ", currencyId=" + currencyId +
                ", amount=" + amount +
                '}';
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public String getSenderCardIdentifier() {
        return senderCardIdentifier;
    }

    public String getReceiverCardIdentifier() {
        return receiverCardIdentifier;
    }

    public int getTransactionTypeId() {
        return transactionTypeId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public void setSenderCardIdentifier(String senderCardIdentifier) {
        this.senderCardIdentifier = senderCardIdentifier;
    }

    public void setReceiverCardIdentifier(String receiverCardIdentifier) {
        this.receiverCardIdentifier = receiverCardIdentifier;
    }

    public void setTransactionTypeId(int transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

}
