package com.example.T_Bank.DAO;

import com.example.T_Bank.DAO.Implementations.AccountDAOImplementation;
import com.example.T_Bank.Storage.Account;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class TBankDAO {
    private BasicDataSource dataSource;
    private Connection connection;
    private AccountDAO accountDao;
    public TBankDAO(){
        dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DatabaseConstants.databaseName);
        dataSource.setUsername(DatabaseConstants.databaseUsername);
        dataSource.setPassword(DatabaseConstants.databasePassword);
        connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        accountDao = new AccountDAOImplementation(connection);
    }

    public Account login(String userName, String password){
        return accountDao.login(userName, password);
    }

    public Account register(String firstName, String lastName, String personalId,
                            String userName, String password, String birthdate){
        return accountDao.register(firstName, lastName, personalId,
                    userName, password, birthdate);
    }
}
