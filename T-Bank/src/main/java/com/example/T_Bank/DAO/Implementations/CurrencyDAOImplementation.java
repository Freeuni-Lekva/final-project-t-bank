package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.CurrencyDAO;
import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.TransferError;

import java.util.ArrayList;

public class CurrencyDAOImplementation implements CurrencyDAO {
    @Override
    public ArrayList<Currency> getCurrencies() {
        return null;
    }

    @Override
    public double getExchangeValue(int amount, Currency fromCurrency, Currency toCurrency) {
        return 0;
    }

    @Override
    public TransferError currencyExchange(String accountNumber, int amountNumber, Currency fromCurrency, Currency toCurrency) {
        return null;
    }
}
