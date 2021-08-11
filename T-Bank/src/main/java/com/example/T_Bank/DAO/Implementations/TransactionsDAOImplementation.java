package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.CurrencyDAO;
import com.example.T_Bank.DAO.DAOInterfaces.TransactionsDAO;
import com.example.T_Bank.Storage.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionsDAOImplementation implements TransactionsDAO {
    private Connection connection;
    private CurrencyDAO currencyDAO;
    public TransactionsDAOImplementation(Connection connection, CurrencyDAO currencyDAO){
        this.connection = connection;
        this.currencyDAO = currencyDAO;
    }

    public boolean accountNumberExists(String accountNumber){
        String checkQuery = "select count(*) from account_cards where card_identifier = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(checkQuery);
            stm.setString(1, accountNumber);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if(count == 1){
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    private String getCurrencyName(Currency fromCurrency){
        if(fromCurrency.getCurrencyName().equals("GEL")){
            return "gel_balance";
        }
        if(fromCurrency.getCurrencyName().equals("USD")){
            return "usd_balance";
        }
        if(fromCurrency.getCurrencyName().equals("EURO")){
            return "euro_balance";
        }
        return "gel_balance";
    }

    private double getAmount(String fromAccountNumber, String currencyQueryName){
        String query = "select " + currencyQueryName + " from account_cards where card_identifier = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, fromAccountNumber);
            ResultSet rs = stm.executeQuery();
            rs.next();
            double amount = rs.getDouble(1);
            return amount;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public TransferError transferMoney(String fromAccountNumber, String toAccountNumber,
                                       double amount, Currency fromCurrency, Currency toCurrency) {
        if(!accountNumberExists(fromAccountNumber)){
            return TransferError.accountNumberDoesNotExist;
        }
        if(!accountNumberExists(toAccountNumber)){
            return TransferError.accountNumberDoesNotExist;
        }
        String currencyQueryName = getCurrencyName(fromCurrency);
        double amountOnAccount = getAmount(fromAccountNumber, currencyQueryName);
        if(amountOnAccount < amount){
            return TransferError.notEnoughAmount;
        }
        String firstPart = "update account_cards set ";
        String secondPart = " = ? where card_identifier = ?";

        try {
            String minusQuery = firstPart + currencyQueryName + secondPart;
            PreparedStatement stm = connection.prepareStatement(minusQuery);
            stm.setDouble(1, amountOnAccount - amount);
            stm.setString(2, fromAccountNumber);
            stm.executeUpdate();

            currencyQueryName = getCurrencyName(toCurrency);
            amountOnAccount = getAmount(toAccountNumber, currencyQueryName);
            double amountToTransfer = currencyDAO.getExchangeValue(amount, fromCurrency, toCurrency);

            String plusQuery = firstPart + currencyQueryName + secondPart;
            stm = connection.prepareStatement(plusQuery);



            stm.setDouble(1, amountToTransfer + amountOnAccount);
            stm.setString(2, toAccountNumber);
            stm.executeUpdate();

            return TransferError.noErrorMessage;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return null;
    }



    @Override
    public AccountNumbersList getAccountNumbers(String personalNumber) {
        String getQuery = "select account_id from accounts where personal_id = ?";
        AccountNumbersList accountNumbersList = null;
        try {
            PreparedStatement stm = connection.prepareStatement(getQuery);
            stm.setString(1, personalNumber);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                int accountId = rs.getInt(1);
                getQuery = "select card_identifier from account_cards where account_id = ?";
                stm = connection.prepareStatement(getQuery);
                stm.setInt(1, accountId);
                rs = stm.executeQuery();
                ArrayList <String> identifiers = new ArrayList<>();
                while(rs.next()){
                    identifiers.add(rs.getString(1));
                }
                return new AccountNumbersList(identifiers, true, TransferError.noErrorMessage);
            }else{
                accountNumbersList = new AccountNumbersList(null, false, TransferError.personalIdDoesNotExist);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new AccountNumbersList(null, false, TransferError.personalIdDoesNotExist);
    }
}
