package com.example.T_Bank.Storage;

public class CardInfo {
    private CardType cardType;
    private int cardTypeId;
    private String cardName;
    private int accountId;
    private double gelBalance;
    private double usdBalance;
    private double euroBalance;
    private int accountCardId;
    private boolean isValidCard;
    private CardErrorMessage errorMessage;

    public CardInfo(int accountCardId, int accountId, int cardTypeId, String cardName,
                    int gelBalance, int usdBalance, int euroBalance, boolean isValidCard,
                    CardErrorMessage errorMessage, CardType cardType) {
        this.cardType = cardType;
        this.cardTypeId = cardTypeId;
        this.cardName = cardName;
        this.accountId = accountId;
        this.gelBalance = gelBalance;
        this.usdBalance = usdBalance;
        this.euroBalance = euroBalance;
        this.accountCardId = accountCardId;
        this.isValidCard = isValidCard;
        this.errorMessage = errorMessage;
    }


    public int getCardTypeId() {
        return cardTypeId;
    }

    public int getAccountCardId() {
        return accountCardId;
    }

    public boolean isValidCard() {
        return isValidCard;
    }

    public CardErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public int getAccountId() {
        return accountId;
    }

    public double getGelBalance() {
        return gelBalance;
    }

    public double getUsdBalance() {
        return usdBalance;
    }

    public double getEuroBalance() {
        return euroBalance;
    }
}
