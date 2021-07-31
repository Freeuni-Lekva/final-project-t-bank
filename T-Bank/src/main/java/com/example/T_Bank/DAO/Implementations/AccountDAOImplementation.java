package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.AccountDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.ErrorMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class AccountDAOImplementation implements AccountDAO {
    private Connection connection;
    ArrayList<Account> accounts;
    public AccountDAOImplementation(Connection connection){
        this.connection = connection;
        accounts = new ArrayList<>();
    }

    private Account badAccount(ErrorMessage errorMessage){
        Account badAccount = new Account(null, null, null, null,
                null, -1, false, errorMessage);
        return badAccount;
    }

    private Account accountFromResultSet(ResultSet rs){
        Account found = null;
        try {
            int accountId = rs.getInt("account_id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String personalId = rs.getString("personal_id");
            String userName = rs.getString("user_name");
            String userPassword = rs.getString("user_password");
            String birthDate = rs.getString("birth_date");
            found = new Account(userName, firstName, lastName, personalId, userPassword,
                    accountId, true, ErrorMessage.NoErrorMessage);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return found;
    }

    @Override
    public Account login(String userName, String password) {
        String loginQuery = "Select * from accounts" +
                " where user_name = ? and user_password = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(loginQuery);
            stm.setString(1, userName);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            if(!rs.next())
                return badAccount(ErrorMessage.AccountNotValid);
            return accountFromResultSet(rs);
        } catch (SQLException throwables) {

        }
        return badAccount(ErrorMessage.AccountNotValid);
    }

    @Override
    public Account register(String firstName, String lastName, String personalId,
                            String userName, String password, String birthdate) {

        String userNameQuery = "select count(*) from accounts where user_name = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(userNameQuery);
            stm.setString(1, userName);
            ResultSet rs = stm.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0){
                return badAccount(ErrorMessage.UserNameAlreadyExists);
            }

            String personalIdQuery = "select count(*) from accounts where personal_id = ?";
            stm = connection.prepareStatement(personalIdQuery);
            stm.setString(1, personalId);
            rs = stm.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0){
                return badAccount(ErrorMessage.PersonalIdAlreadyExists);
            }

            String registerQuery = "insert into accounts " +
                    " (first_name, last_name, personal_id," +
                    " user_name, user_password, birth_date) " +
                    "values ( ?, ?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(registerQuery);
            stm.setString(1, firstName);
            stm.setString(2, lastName);
            stm.setString(3, personalId);
            stm.setString(4, userName);
            stm.setString(5, password);
            stm.setString(6, null);
            stm.execute();
            return login(userName, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return badAccount(ErrorMessage.UserNameAlreadyExists);
    }
}
