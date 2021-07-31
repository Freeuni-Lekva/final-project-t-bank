package com.example.T_Bank.Storage;

public class CardType {
    private int cardTypeId;
    private String cardTypeName;
    private int maxLimit;
    private String description;

    public CardType(int cardTypeId, String cardTypeName, int maxLimit, String description) {
        this.cardTypeId = cardTypeId;
        this.cardTypeName = cardTypeName;
        this.maxLimit = maxLimit;
        this.description = description;
    }

    public int getCardTypeId() {
        return cardTypeId;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public String getDescription() {
        return description;
    }
}
