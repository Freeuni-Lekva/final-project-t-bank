package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.TransactionsDAO;
import com.example.T_Bank.Storage.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
