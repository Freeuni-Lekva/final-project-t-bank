package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.DepositDAO;
import com.example.T_Bank.Storage.Deposit;

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
                Deposit curr = new Deposit(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4),
                        res.getDouble(5), res.getInt(6), res.getDouble(7),
                        res.getInt(8), res.getTimestamp(9), res.getTimestamp(10),
                        res.getTimestamp(11), res.getBoolean(12));
                result.add(curr);
            }
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    @Override
    public void openDeposit(int accountID, String cardIdentifier, int periods, double amount, String depositName) {
        String addQuery = "insert into account_deposits(deposit_name, account_id, card_identifier, balance, currency_id, percent, " +
                "periods, start_date, last_update_date, end_date, active)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Timestamp startTime = new Timestamp(System.currentTimeMillis());
            long current = startTime.getTime();
            Timestamp endTime = new Timestamp(current + periods * MINUTES_PERIOD);
            PreparedStatement stm = connection.prepareStatement(addQuery);
            stm.setString(1, depositName);
            stm.setInt(2, accountID);
            stm.setString(3, cardIdentifier);
            stm.setDouble(4, 0);
            stm.setInt(5, 0);
            stm.setInt(6, 15);
            stm.setDouble(7, periods);
            stm.setTimestamp(8, startTime);
            stm.setTimestamp(9, startTime);
            stm.setTimestamp(10, endTime);
            stm.setBoolean(11, true);
            stm.executeUpdate();

            updateCard(cardIdentifier, 0, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void addAmountToDeposit(int depositID, double amount) throws SQLException {
        String query = "update account_deposits set balance = balance + ? where deposit_id = ?";
        PreparedStatement stm = connection.prepareStatement(query);
        stm.setDouble(1, amount);
        stm.setInt(2, depositID);
        stm.executeUpdate();

        query = "select card_identifier, currency_id from account_deposits where deposit_id = ?";
        stm = connection.prepareStatement(query);
        stm.setInt(1, depositID);

        ResultSet rs = stm.executeQuery();
        rs.next();

        updateCard(rs.getString("card_identifier"), rs.getInt("currency_id"), amount);
    }

    @Override
    public void closeDeposit(int depositID) {
        try {
            String closeDepositQuery = "update account_deposits " +
                    "active = ? " +
                    "where deposit_id = ? ";
            PreparedStatement stm = connection.prepareStatement(closeDepositQuery);
            stm.setBoolean(1, false);
            stm.setInt(2, depositID);
            stm.executeUpdate();

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
