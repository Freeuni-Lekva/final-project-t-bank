package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.TransactionsDAO;
import com.example.T_Bank.Storage.AccountNumbersList;
import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.TransferError;

import java.sql.Connection;

public class TransactionsDAOImplementation implements TransactionsDAO {
    private Connection connection;
    public TransactionsDAOImplementation(Connection connection){
        this.connection = connection;
    }
    @Override
    public TransferError transferMoney(String fromAccountNumber, String toAccountNumber, int amount, Currency fromCurrency, Currency toCurrency) {
        return null;
    }

    @Override
    public AccountNumbersList getAccountNumbers(String personalNumber) {
        return null;
    }
}
