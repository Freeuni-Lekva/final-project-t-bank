package com.example.T_Bank.DAO;

import com.example.T_Bank.DAO.DAOInterfaces.AccountDAO;
import com.example.T_Bank.DAO.DAOInterfaces.CardDAO;
import com.example.T_Bank.DAO.DAOInterfaces.CurrencyDAO;
import com.example.T_Bank.DAO.DAOInterfaces.TransactionsDAO;
import com.example.T_Bank.DAO.Implementations.AccountDAOImplementation;
import com.example.T_Bank.DAO.Implementations.CardDAOImplementation;
import com.example.T_Bank.Storage.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TBankDAO implements AccountDAO, CardDAO, TransactionsDAO, CurrencyDAO {
    private BasicDataSource dataSource;
    private Connection connection;
    private AccountDAO accountDao;

    @Override
    public ArrayList<Currency> getCurrencies() {
        return null;
    }

    @Override
    public double getExchangeValue(int amount, Currency fromCurrency, Currency toCurrency) {
        return 0;
    }

    @Override
    public TransferError currencyExchange(String accountNumber, int amountNumber, Currency fromCurrency, Currency toCurrency) {
        return null;
    }

    @Override
    public TransferError transferMoney(String fromAccountNumber, String toAccountNumber, int amount, Currency fromCurrency, Currency toCurrency) {
        return null;
    }

    @Override
    public AccountNumbersList getAccountNumbers(String personalNumber) {
        return null;
    }

    private CardDAO cardDao;
    public TBankDAO() {
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
        cardDao = new CardDAOImplementation(connection);
    }

    public CardInfo addCard(int accountId, CardType cardType, String cardName){
        return cardDao.addCard(accountId, cardType, cardName);
    }
    public List<CardType> getCardTypes(){
        return cardDao.getCardTypes();
    }
    public List<CardInfo> getAccountCards(int accountId){
        return cardDao.getAccountCards(accountId);
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
