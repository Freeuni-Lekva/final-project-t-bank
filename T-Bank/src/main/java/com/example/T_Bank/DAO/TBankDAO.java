package com.example.T_Bank.DAO;

import com.example.T_Bank.DAO.DAOInterfaces.*;
import com.example.T_Bank.DAO.Implementations.*;
import com.example.T_Bank.Storage.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TBankDAO implements AccountDAO, CardDAO, TransactionsDAO, CurrencyDAO, CrowdFundingEventDAO {
    private BasicDataSource dataSource;
    private Connection connection;
    private AccountDAO accountDao;
    private TransactionsDAO transactionsDAO;
    private CurrencyDAO currencyDAO;
    private CrowdFundingEventDAO crowdFundingEventDAO;

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
        currencyDAO = new CurrencyDAOImplementation(connection);
        transactionsDAO = new TransactionsDAOImplementation(connection, currencyDAO);
        crowdFundingEventDAO = new CrowdFundingEventDAOImplementation(connection);
    }

    public CardInfo addCard(int accountId, CardType cardType, String cardName) {
        return cardDao.addCard(accountId, cardType, cardName);
    }

    public List<CardType> getCardTypes() {
        return cardDao.getCardTypes();
    }

    public List<CardInfo> getAccountCards(int accountId) {
        return cardDao.getAccountCards(accountId);
    }

    public Account login(String userName, String password) {
        return accountDao.login(userName, password);
    }

    public Account register(String firstName, String lastName, String personalId,
                            String userName, String password, String birthdate) {
        return accountDao.register(firstName, lastName, personalId,
                userName, password, birthdate);
    }

    @Override
    public ArrayList<Currency> getCurrencies() {
        return currencyDAO.getCurrencies();
    }

    @Override
    public double getExchangeValue(double amount, Currency fromCurrency, Currency toCurrency) {
        return currencyDAO.getExchangeValue(amount, fromCurrency, toCurrency);
    }

    @Override
    public TransferError currencyExchange(String accountNumber, double amountNumber, Currency fromCurrency, Currency toCurrency) {
        return currencyDAO.currencyExchange(accountNumber, amountNumber, fromCurrency, toCurrency);
    }

    @Override
    public TransferError transferMoney(String fromAccountNumber, String toAccountNumber, double amount, Currency fromCurrency, Currency toCurrency) {
        return transactionsDAO.transferMoney(fromAccountNumber, toAccountNumber, amount, fromCurrency, toCurrency);
    }

    @Override
    public AccountNumbersList getAccountNumbers(String personalNumber) {
        return transactionsDAO.getAccountNumbers(personalNumber);
    }

    @Override
    public EventError createCrowdFundingEvent(String eventName, int accountId, String cardIdentifier, String description, double targetMoney) {
        return crowdFundingEventDAO.createCrowdFundingEvent(eventName, accountId, cardIdentifier,
                description, targetMoney);
    }

    @Override
    public EventError deleteCrowdFundingEvent(int eventId) {
        return crowdFundingEventDAO.deleteCrowdFundingEvent(eventId);
    }

    @Override
    public EventError changeEventTarget(int eventId, double changedTarget) {
        return crowdFundingEventDAO.changeEventTarget(eventId, changedTarget);
    }

    @Override
    public ArrayList<EventError> getPublicCrowdFundingEvents() {
        return crowdFundingEventDAO.getPublicCrowdFundingEvents();
    }

    @Override
    public ArrayList<EventError> getSpecificEvents(String personalNumber) {
        return crowdFundingEventDAO.getSpecificEvents(personalNumber);
    }
}
