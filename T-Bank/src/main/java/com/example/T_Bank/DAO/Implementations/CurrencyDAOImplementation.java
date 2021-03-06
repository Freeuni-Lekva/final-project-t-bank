package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.CurrencyDAO;
import com.example.T_Bank.DAO.DAOInterfaces.TransactionHistoryDAO;
import com.example.T_Bank.Storage.CardErrorMessage;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.TransferError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrencyDAOImplementation implements CurrencyDAO {
    private Connection connection;
    private TransactionHistoryDAO transactionHistoryDAO;
    public CurrencyDAOImplementation(Connection connection,TransactionHistoryDAO transactionHistoryDAO){
        this.connection = connection;
        this.transactionHistoryDAO=transactionHistoryDAO;
    }

   /* @Override
    public ArrayList<Currency> getCurrencies(String accountNumber) {
        ArrayList<Currency> clientCurrencies = new ArrayList<>();

        String checkBalancesQuery = "select * from account_balances where account_number = ?";
        try {
            PreparedStatement checkBalancesStm = connection.prepareStatement(checkBalancesQuery);
            checkBalancesStm.setString(1, accountNumber);
            ResultSet availableBalances = checkBalancesStm.executeQuery();

            while (availableBalances.next()) {
                String currencyName = availableBalances.getString("currecy_name");
                String checkCurrencyQuery = "select * from currency_exchange where currency_name = ?";
                PreparedStatement checkCurrencyStm = connection.prepareStatement(checkBalancesQuery);
                checkCurrencyStm.setString(1, currencyName);
                ResultSet availableCurrencies = checkCurrencyStm.executeQuery();

                while (availableCurrencies.next()) {
                    int currencyIndex = availableCurrencies.getInt("currency_id");
                    double currencyCall = availableCurrencies.getDouble("call_price");
                    double currencyBid = availableCurrencies.getDouble("bid_price");
                    Currency currCurrency = new Currency(currencyName, currencyIndex, currencyCall, currencyBid);
                    clientCurrencies.add(currCurrency);
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return clientCurrencies;
    }*/

    @Override
    public String getCurrencyName(int currencyId) {
        String query = "select currency_name from currency_exchange where currency_id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, currencyId);
            ResultSet res = stm.executeQuery();
            res.next();
            return res.getString(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    @Override
    public ArrayList<Currency> getCurrencies() {
        ArrayList<Currency> currencies = new ArrayList<>();

        String checkQuery = "select * from currency_exchange";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkQuery);
            ResultSet availableCurrencies = checkStm.executeQuery();

            while (availableCurrencies.next()) {
                String currencyName = availableCurrencies.getString("currency_name");
                int currencyIndex = availableCurrencies.getInt("currency_id");
                double currencyCall = availableCurrencies.getDouble("call_price");
                double currencyBid = availableCurrencies.getDouble("bid_price");
                Currency currCurrency = new Currency(currencyName, currencyIndex, currencyCall, currencyBid);
                currencies.add(currCurrency);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return currencies;
    }

    /*@Override
    public double getExchangeValue(double sentAmount, double receivedAmount, Currency fromCurrency, Currency toCurrency) {

        *//*double bidValue = fromCurrency.getBid();
        double callValue = toCurrency.getCall();*//*

        String checkBidQuery = "select bid_price from currency_exchange where currency_name = ?";
        String checkCallQuery = "select bid_price from currency_exchange where currency_name = ?";
        try {
            PreparedStatement checkBidStm = connection.prepareStatement(checkBidQuery);
            checkBidStm.setString(1, fromCurrency.getCurrencyName());
            PreparedStatement checkCallStm = connection.prepareStatement(checkCallQuery);
            checkBidStm.setString(1, toCurrency.getCurrencyName());

            double bidValue = checkBidStm.executeQuery().first();
            double callValue = checkCallStm.executeQuery().first();

            double rate = Calculate(bidValue, callValue)

            if (sentAmount == 0 && receivedAmount != 0) {
                rate = (1 / rate);
            }

            return rate * amount;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }*/

    @Override
    public double getExchangeValue(double amount, Currency fromCurrency, Currency toCurrency) {
        double bidValue = fromCurrency.getBid();
        double callValue = toCurrency.getCall();

        double exchangedAmount = Calculate(bidValue, callValue) * amount;
        return exchangedAmount;
    }

    private double Calculate(double bidValue, double callValue) {
        return bidValue / callValue;
    }

    @Override
    public TransferError currencyExchange(String accountNumber, double amountNumber, Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency == toCurrency) {
            return TransferError.similarCurrenciesError;
        }

        double exchangedAmount = getExchangeValue(amountNumber, fromCurrency, toCurrency);

        String sendingBalance = fromCurrency.getCurrencyName().toLowerCase() + "_balance";
        String receivingBalance = toCurrency.getCurrencyName().toLowerCase() + "_balance";

        String updateSendingBalanceQuery = updateBalanceQuery(sendingBalance);
        String updateReceivingBalanceQuery = updateBalanceQuery(receivingBalance);

        String selectSendingBalanceQuery = "select " + sendingBalance + " from account_cards where card_identifier = ?";
        try {
            PreparedStatement selectSendingBalanceStm = connection.prepareStatement(selectSendingBalanceQuery);
            selectSendingBalanceStm.setString(1, accountNumber);

            ResultSet sendingBalanceAmounts = selectSendingBalanceStm.executeQuery();
            sendingBalanceAmounts.next();
            double sendingBalanceAmount = sendingBalanceAmounts.getDouble(1);


            if (sendingBalanceAmount < amountNumber) {
                return TransferError.notEnoughAmount;
            }

            PreparedStatement updateSendingBalanceStm = connection.prepareStatement(updateSendingBalanceQuery);
            PreparedStatement updateReceivingBalanceStm = connection.prepareStatement(updateReceivingBalanceQuery);

            setValues(updateReceivingBalanceStm, exchangedAmount, accountNumber, false);
            setValues(updateSendingBalanceStm, amountNumber, accountNumber, true);

            updateReceivingBalanceStm.executeUpdate();
            updateSendingBalanceStm.executeUpdate();

            int accountId=getAccountId(accountNumber);
            transactionHistoryDAO.logTransaction(accountId,accountId,accountNumber,accountNumber,2,
                    new Date(System.currentTimeMillis()),toCurrency.getCurrencyId(),getExchangeValue(amountNumber,fromCurrency,toCurrency));
            transactionHistoryDAO.logTransaction(accountId,accountId,accountNumber,accountNumber,2,
                    new Date(System.currentTimeMillis()),fromCurrency.getCurrencyId(),-amountNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return TransferError.noErrorMessage;
    }

    private int getAccountId(String accountNumber) {
        int accountId=0;
        try {
            PreparedStatement statement=connection.prepareStatement("select * from account_cards where card_identifier=?");
            statement.setString(1,accountNumber);
            ResultSet result=statement.executeQuery();
            result.next();
            accountId=result.getInt("account_id");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return accountId;

    }


    private void setValues(PreparedStatement stm, double amount, String accountNumber, boolean isSending) {
        try {
            stm.setString(2, accountNumber);
            if (isSending) {
                stm.setDouble(1, -amount);
            } else {
                stm.setDouble(1, amount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String updateBalanceQuery (String balance) {
        String updateBalanceQuery0 = "update account_cards set ";
        String updateBalanceQuery1 = " + ? where card_identifier = ?";

        String updateBalanceQuery = updateBalanceQuery0 + balance + " = " + balance + updateBalanceQuery1;
        return updateBalanceQuery;
    }
}
