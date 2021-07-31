package com.example.T_Bank.Storage;

public class CardInfo {
    private String cardType;
    private String cardName;
    private int accountId;
    private double gelBalance;
    private double usdBalance;
    private double euroBalance;
    private int accountCardId;
    private boolean isValidCard;
    private CardErrorMessage errorMessage;

    public CardInfo(int accountCardId, String cardType, String cardName, int accountId,
                    double gelBalance, double usdBalance, double euroBalance, boolean isValidCard,
                    CardErrorMessage errorMessage) {
        this.cardType = cardType;
        this.cardName = cardName;
        this.accountId = accountId;
        this.gelBalance = gelBalance;
        this.usdBalance = usdBalance;
        this.euroBalance = euroBalance;
        this.accountCardId = accountCardId;
        this.isValidCard = isValidCard;
        this.errorMessage = errorMessage;
    }

    public String getCardType() {
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
