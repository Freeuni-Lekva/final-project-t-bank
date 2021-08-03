package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.TransferError;

import java.util.ArrayList;

public interface CurrencyDAO {
    public ArrayList<Currency> getCurrencies();
    public double getExchangeValue(int amount, Currency fromCurrency, Currency toCurrency);
    public TransferError currencyExchange(String accountNumber, int amountNumber,
                                          Currency fromCurrency, Currency toCurrency);

}
