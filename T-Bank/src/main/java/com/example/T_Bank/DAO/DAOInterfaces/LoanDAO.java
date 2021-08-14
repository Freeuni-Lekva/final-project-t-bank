package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.Loan;
import com.example.T_Bank.Storage.LoanErrorMessage;
import com.example.T_Bank.Storage.LoanList;

public interface LoanDAO {
    public LoanErrorMessage takeLoan(int accountId, String cardIdentifier, double startMoney, int periods);
    public LoanList getAllLoans(int accountId);
}
