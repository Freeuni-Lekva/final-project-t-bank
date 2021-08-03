package com.example.T_Bank.Storage;

import java.util.ArrayList;

public class AccountNumbersList {
    private ArrayList<String> accountNumbers;
    private boolean isValid;
    private TransferError errorMessage;

    public AccountNumbersList(ArrayList<String> accountNumbers, boolean isValid, TransferError errorMessage) {
        this.accountNumbers = accountNumbers;
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public ArrayList<String> getAccountNumbers() {
        return accountNumbers;
    }

    public boolean isValid() {
        return isValid;
    }

    public TransferError getErrorMessage() {
        return errorMessage;
    }
}
