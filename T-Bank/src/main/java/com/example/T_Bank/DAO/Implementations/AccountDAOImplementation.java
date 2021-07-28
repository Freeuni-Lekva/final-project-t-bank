package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.AccountDAO;
import com.example.T_Bank.Storage.Account;

import java.sql.Connection;

public class AccountDAOImplementation implements AccountDAO {
    private Connection connection;

    public AccountDAOImplementation(Connection connection){
        this.connection = connection;
    }

    @Override
    public Account login() {
        return null;
    }

    @Override
    public void register() {

    }
}
