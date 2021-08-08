package com.example.T_Bank.Storage;

public class CrowdFundingEvent {
    private int eventId;
    private String eventName;
    private int accountId;
    private String cardIdentifier;
    private String description;
    private double target;
    private double done;
    private boolean isActive;
    private Currency currency;

    public CrowdFundingEvent(int eventId, String eventName, int accountId,
                             String cardIdentifier, String description,
                             double target, double done, boolean isActive, Currency currency) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.accountId = accountId;
        this.cardIdentifier = cardIdentifier;
        this.description = description;
        this.target = target;
        this.done = done;
        this.isActive = isActive;
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public double getTarget() {
        return target;
    }

    public double getDone() {
        return done;
    }

    public boolean isActive() {
        return isActive;
    }
}
