package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.AccountNumbersList;
import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.TransferError;

import java.util.ArrayList;

public interface TransactionsDAO {
    public TransferError transferMoney(String fromAccountNumber, String toAccountNumber,
                                       int amount, Currency fromCurrency, Currency toCurrency);
    public AccountNumbersList getAccountNumbers(String personalNumber);
}
