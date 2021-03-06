package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.DepositDAO;
import com.example.T_Bank.Storage.Deposit;
import com.example.T_Bank.Storage.DepositError;
import com.example.T_Bank.Storage.LoanErrorMessage;

import java.sql.*;
import java.util.ArrayList;

public class DepositDAOImplementation implements DepositDAO{
    private Connection connection;
    private static final long MINUTES_PERIOD = 1 * 1000 * 60;

    public DepositDAOImplementation(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ArrayList<Deposit> getAllDeposits(int accountID) {
        ArrayList<Deposit> result = new ArrayList<>();
        String query = "select * from account_deposits where account_id = ? and active = true";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, accountID);
            ResultSet res = stm.executeQuery();
            while (res.next()) {
                Deposit curr = new Deposit(res.getInt(1), res.getString(2), res.getInt(3),
                        res.getString(4), res.getDouble(5),
                        res.getInt(6), res.getDouble(7),
                        res.getInt(8), res.getTimestamp(9), res.getTimestamp(10),
                        res.getTimestamp(11), res.getBoolean(12),
                        res.getDouble(13));
                result.add(curr);
            }
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    private boolean checkColumnValidity(String columnName, String table, String strValue, int intValue){
        String checkQuery = "select count(*) from " + table + " where " + columnName + " = ?";
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(checkQuery);
            if(strValue == null){
                stm.setInt(1, intValue);
            }else{
                stm.setString(1, strValue);
            }
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if(count == 0){
                return false;
            }
            if(count == 1){
                return true;
            }
            if(count > 1){
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public DepositError openDeposit(int accountID, String cardIdentifier, int periods, double amount, String depositName) {
        if(!checkColumnValidity("account_id", "accounts", null, accountID)){
            return DepositError.accountIdNotValid;
        }

        if(!checkColumnValidity("card_identifier", "account_cards", cardIdentifier, 0)){
            return DepositError.cardNotValid;
        }

        if (periods < 0) {
            return DepositError.periodsLessThanZero;
        }

        if (amount < 0) {
            return DepositError.initialNegativeDeposit;
        }

        String checkQuery = "select gel_balance from account_cards where card_identifier = ?";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkQuery);
            checkStm.setString(1, cardIdentifier);
            ResultSet res = checkStm.executeQuery();
            res.next();
            if (res.getDouble(1) < amount) {
                return DepositError.notEnoughOnBalance;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String addQuery = "insert into account_deposits(deposit_name, account_id, card_identifier, balance, currency_id, percent, " +
                "periods, start_date, last_update_date, end_date, active,start_money)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try {
            Timestamp startTime = new Timestamp(System.currentTimeMillis());
            long current = startTime.getTime();
            Timestamp endTime = new Timestamp(current + periods * MINUTES_PERIOD);
            PreparedStatement stm = connection.prepareStatement(addQuery);
            stm.setString(1, depositName);
            stm.setInt(2, accountID);
            stm.setString(3, cardIdentifier);
            stm.setDouble(4, amount);
            stm.setInt(5, 1);
            stm.setDouble(6, 15);
            stm.setDouble(7, periods);
            stm.setTimestamp(8, startTime);
            stm.setTimestamp(9, startTime);
            stm.setTimestamp(10, endTime);
            stm.setBoolean(11, true);
            stm.setDouble(12, amount);
            stm.executeUpdate();
            updateCard(cardIdentifier, 1, -amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return DepositError.noErrorMessage;
    }

    @Override
    public void addAmountToDeposit(int depositID, double amount) throws SQLException {
        String query = "update account_deposits set balance = balance + ? where deposit_id = ? and active = true";
        PreparedStatement stm = connection.prepareStatement(query);
        stm.setDouble(1, amount);
        stm.setInt(2, depositID);
        if (stm.executeUpdate() == 0) {
            return;
        }
        query = "select card_identifier, currency_id from account_deposits where deposit_id = ?";
        stm = connection.prepareStatement(query);
        stm.setInt(1, depositID);

        ResultSet rs = stm.executeQuery();
        rs.next();

        updateCard(rs.getString("card_identifier"), rs.getInt("currency_id"), -amount);
    }

    @Override
    public void closeDeposit(int depositID) {
        try {
            String closeDepositQuery = "update account_deposits " +
                    "set active = false " +
                    "where deposit_id = ? and active = true";
            PreparedStatement stm = connection.prepareStatement(closeDepositQuery);
            stm.setInt(1, depositID);
            if (stm.executeUpdate() == 0) {
                return;
            }

            String query = "select card_identifier, balance, currency_id from account_deposits where deposit_id = ?";
            stm = connection.prepareStatement(query);
            stm.setInt(1, depositID);
            ResultSet rs = stm.executeQuery();
            rs.next();

            String cardIdentifier = rs.getString(1);
            double balance = rs.getDouble(2);
            updateCard(cardIdentifier, rs.getInt(3), balance);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void updateCard(String card, int currencyId, double balance) {
        String currency;
        String checkQuery = "select currency_name from currency_exchange where currency_id = ? ";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkQuery);
            checkStm.setInt(1, currencyId);
            ResultSet result = checkStm.executeQuery();
            result.next();
            currency = result.getString(1).toLowerCase() + "_balance";
            String cardUpdate = "update account_cards set " + currency + " = ? where card_identifier = ?";
            String query = "select " + currency + " from account_cards where card_identifier = ?";
            try {
                PreparedStatement queryStm = connection.prepareStatement(query);
                queryStm.setString(1, card);
                ResultSet res = queryStm.executeQuery();
                res.next();
                double currencyBalance = res.getDouble(1);

                PreparedStatement cardStm = connection.prepareStatement(cardUpdate);
                cardStm.setDouble(1, currencyBalance + balance);
                cardStm.setString(2, card);
                cardStm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
