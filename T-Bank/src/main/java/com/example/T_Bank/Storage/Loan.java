package com.example.T_Bank.Storage;

import java.util.Date;

public class Loan {
    private int accountLoanId;
    private int accountId;
    private String cardIdentifier;
    private double startMoney;
    private double percent;
    private int periods;
    private double fullMoney;
    private Date startDate;
    private Date lastUpdateDate;
    private Date endDate;
    private boolean activeLoan;
    private boolean isValid;
    private LoanErrorMessage errorMessage;
    private double monthlyPayment;
    private double payedAmount;
    private double toPayAmount;

    public Loan(int accountLoanId, int accountId, String cardIdentifier, double startMoney,
                double percent, int periods, double fullMoney, double monthlyPayment, Date startDate, Date lastUpdateDate,
                Date endDate, boolean activeLoan, boolean isValid, LoanErrorMessage errorMessage,
                double payedAmount, double toPayAmount) {
        this.accountLoanId = accountLoanId;
        this.accountId = accountId;
        this.cardIdentifier = cardIdentifier;
        this.startMoney = startMoney;
        this.percent = percent;
        this.periods = periods;
        this.fullMoney = fullMoney;
        this.startDate = startDate;
        this.lastUpdateDate = lastUpdateDate;
        this.endDate = endDate;
        this.activeLoan = activeLoan;
        this.isValid = isValid;
        this.errorMessage = errorMessage;
        this.monthlyPayment = monthlyPayment;
        this.payedAmount = payedAmount;
        this.toPayAmount = toPayAmount;
    }

    public int getAccountLoanId() {
        return accountLoanId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public double getStartMoney() {
        return startMoney;
    }

    public double getPercent() {
        return percent;
    }

    public int getPeriods() {
        return periods;
    }

    public double getFullMoney() {
        return fullMoney;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isActiveLoan() {
        return activeLoan;
    }

    public boolean isValid() {
        return isValid;
    }

    public double getPayedAmount() {
        return payedAmount;
    }

    public double getToPayAmount() {
        return toPayAmount;
    }

    public LoanErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }
}
