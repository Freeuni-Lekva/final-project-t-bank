package com.example.T_Bank.Storage;

import java.util.ArrayList;

public class LoanList {
    private ArrayList<Loan> loans;
    private boolean isValid;
    private LoanErrorMessage errorMessage;

    public LoanList(ArrayList<Loan> loans, boolean isValid, LoanErrorMessage errorMessage) {
        this.loans = loans;
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public ArrayList<Loan> getLoans() {
        return loans;
    }

    public boolean isValid() {
        return isValid;
    }

    public LoanErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
