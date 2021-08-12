package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.TransactionHistoryDAO;
import com.example.T_Bank.Storage.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;

public class TransactionHistoryDAOImplementation implements TransactionHistoryDAO {
    private Connection connection;

    public TransactionHistoryDAOImplementation(Connection connection) {
        this.connection=connection;
    }
    public ArrayList<Transaction> getAllTransactions(int accountId) {
        ArrayList<Transaction> allTransactions=new ArrayList<>();
        try {
            PreparedStatement statement=connection.prepareStatement("select * from account_logs where sender_account_id=? or receiver_account_id=?");
            statement.setInt(1,accountId);
            statement.setInt(2,accountId);
            ResultSet results= statement.executeQuery();
            while(results.next()) {
                addTransaction(results,allTransactions);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allTransactions;
    }

    public ArrayList<Transaction> getMoneyTransfers(int accountId) {
        ArrayList<Transaction> transfers=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from account_logs where transaction_type_id=? and (sender_account_id=? or receiver_account_id=?)");
            statement.setInt(1,1);
            statement.setInt(2,accountId);
            statement.setInt(3,accountId);
            ResultSet results= statement.executeQuery();
            while(results.next()) {
               addTransaction(results,transfers);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return transfers;
    }

    public ArrayList<Transaction> getAllIncomes(int accountId) {
        ArrayList<Transaction> incomes=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from account_logs where receiver_account_id=? and transaction_type_id=?");
            statement.setInt(1,accountId);
            statement.setInt(2,1);
            ResultSet results= statement.executeQuery();
            while(results.next()) {
                addTransaction(results,incomes);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return incomes;

    }

    public ArrayList<Transaction> getAllExpenses(int accountId) {
        ArrayList<Transaction> expenses=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from account_logs where sender_account_id=? and transaction_type_id=?");
            statement.setInt(1,accountId);
            statement.setInt(2,1);
            ResultSet results= statement.executeQuery();
            while(results.next()) {
                addTransaction(results,expenses);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return expenses;
    }

    public ArrayList<Transaction> getAllConversions(int accountId) {
        ArrayList<Transaction> conversions=new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from account_logs where transaction_type_id=? and (sender_account_id=? or receiver_account_id=?)");
            statement.setInt(1,2);
            statement.setInt(2,accountId);
            statement.setInt(3,accountId);
            ResultSet results= statement.executeQuery();
            while(results.next()) {
                addTransaction(results,conversions);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return conversions;

    }

    public Transaction logTransaction(int senderAccountId, int receiverAccountId, String senderCardIdentifier, String receiverCardIdentifier, int transactionTypeId, Date date, int currencyId,double amount) {
        try {
            PreparedStatement statement=connection.prepareStatement("insert into account_logs (sender_account_id,receiver_account_id," +
                    "sender_card_identifier,receiver_card_identifier,transaction_type_id,log_date,currency_id,amount) VALUES(?,?,?,?,?,?,?,?)");
            statement.setInt(1,senderAccountId);
            statement.setInt(2,receiverAccountId);
            statement.setString(3,senderCardIdentifier);
            statement.setString(4,receiverCardIdentifier);
            statement.setInt(5,transactionTypeId);
            statement.setDate(6, (java.sql.Date) date);
            statement.setInt(7,currencyId);
            statement.setDouble(8,amount);
            System.out.println(statement);
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
       return null;
    }

    private void addTransaction(ResultSet results, ArrayList<Transaction> transactions) throws SQLException {
            int senderAccountId = results.getInt(2);
            int receiverAccountId = results.getInt(3);
            String senderCardIdentifier = results.getString(4);
            String receiverCardIdentifier = results.getString(5);
            int transactionTypeId = results.getInt(6);
            Date date = results.getDate(7);
            int currencyId=results.getInt(8);
            double amount=results.getDouble(9);
            Transaction transaction = new Transaction(senderAccountId, receiverAccountId, senderCardIdentifier, receiverCardIdentifier, transactionTypeId, date,currencyId,amount);
            transactions.add(transaction);
    }
}
