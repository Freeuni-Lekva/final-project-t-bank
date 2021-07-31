package com.example.T_Bank.DAO;

import com.example.T_Bank.DAO.Implementations.AccountDAOImplementation;
import com.example.T_Bank.DAO.Implementations.CardDAOImplementation;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class TBankDAO {
    private BasicDataSource dataSource;
    private Connection connection;
    private AccountDAO accountDao;
    private CardDAO cardDao;
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
        if(connection != null){
            System.out.println("connection not null");
        }
        accountDao = new AccountDAOImplementation(connection);
        cardDao = new CardDAOImplementation(connection);
    }

    public CardInfo addCard(int accountId, CardType cardType, String cardName){
        return null;
    }
    public List<CardType> getCardTypes(){
        return null;
    }
    public List<CardInfo> getAccountCards(int accountId){
        return null;
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
