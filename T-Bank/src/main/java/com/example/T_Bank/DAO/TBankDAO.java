package com.example.T_Bank.DAO;

import com.example.T_Bank.DAO.DAOInterfaces.*;
import com.example.T_Bank.DAO.Implementations.*;
import com.example.T_Bank.Storage.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class TBankDAO implements AccountDAO, CardDAO, TransactionsDAO, CurrencyDAO,
        CrowdFundingEventDAO,TransactionHistoryDAO, LoanDAO, DepositDAO {
    private BasicDataSource dataSource;
    private Connection connection;
    private AccountDAO accountDao;
    private TransactionsDAO transactionsDAO;
    private CurrencyDAO currencyDAO;
    private CrowdFundingEventDAO crowdFundingEventDAO;
    private TransactionHistoryDAO transactionHistoryDAO;
    private LoanDAO loanDAO;
    private DepositDAO depositDAO;

    private CardDAO cardDao;
    private Timer timer;

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
        transactionHistoryDAO=new TransactionHistoryDAOImplementation(connection);
        accountDao = new AccountDAOImplementation(connection);
        cardDao = new CardDAOImplementation(connection);
        currencyDAO = new CurrencyDAOImplementation(connection,transactionHistoryDAO);
        transactionsDAO = new TransactionsDAOImplementation(connection, currencyDAO,transactionHistoryDAO);
        crowdFundingEventDAO = new CrowdFundingEventDAOImplementation(connection, currencyDAO);
        loanDAO = new LoanDAOImplementation(connection);
        timer = new Timer();
        BankDataUpdater bankDataUpdater = new BankDataUpdater(connection);
        timer.schedule(bankDataUpdater, new Date(), 30000);
        depositDAO = new DepositDAOImplementation(connection);
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
    public EventError createCrowdFundingEvent(String eventName, int accountId,
                                              String cardIdentifier, String description,
                                              double targetMoney, Currency currency) {
        return crowdFundingEventDAO.createCrowdFundingEvent(eventName, accountId, cardIdentifier,
                description, targetMoney, currency);
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
    public ArrayList<CrowdFundingEvent> getPublicCrowdFundingEvents() {
        return crowdFundingEventDAO.getPublicCrowdFundingEvents();
    }

    @Override
    public EventList getSpecificEvents(String personalNumber) {
        return crowdFundingEventDAO.getSpecificEvents(personalNumber);
    }

    @Override
    public EventError sendFunds(int eventId, String fromCardIdentifier, double amount, Currency fromCurrency) {
        return crowdFundingEventDAO.sendFunds(eventId, fromCardIdentifier, amount, fromCurrency);
    }


    @Override
    public ArrayList<Transaction> getAllTransactions(int accountId) {
        return transactionHistoryDAO.getAllTransactions(accountId);
    }

    @Override
    public ArrayList<Transaction> getMoneyTransfers(int accountId) {
        return transactionHistoryDAO.getMoneyTransfers(accountId);
    }

    @Override
    public ArrayList<Transaction> getAllIncomes(int accountId) {
        return transactionHistoryDAO.getAllIncomes(accountId);
    }

    @Override
    public ArrayList<Transaction> getAllExpenses(int accountId) {
        return transactionHistoryDAO.getAllExpenses(accountId);
    }

    @Override
    public ArrayList<Transaction> getAllConversions(int accountId) {
        return transactionHistoryDAO.getAllConversions(accountId);
    }

    @Override
    public Transaction logTransaction(int senderAccountId, int receiverAccountId, String senderCardIdentifier, String receiverCardIdentifier, int transactionTypeId, Date date,int currencyId, double amount) {
        return transactionHistoryDAO.logTransaction(senderAccountId,receiverAccountId,senderCardIdentifier,receiverCardIdentifier,transactionTypeId,date,currencyId,amount);
    }

    @Override
    public String getCurrencyName(int currencyId) {
        return currencyDAO.getCurrencyName(currencyId);
    }

    @Override
    public LoanErrorMessage takeLoan(int accountId, String cardIdentifier, double startMoney, int periods) {
        return loanDAO.takeLoan(accountId, cardIdentifier, startMoney, periods);
    }

    @Override
    public LoanList getAllLoans(int accountId) {
        return loanDAO.getAllLoans(accountId);
    }

    @Override
    public void closeDeposit(int depositID) {
        depositDAO.closeDeposit(depositID);
    }

    @Override
    public DepositError openDeposit(int accountID, String cardIdentifier, int periods, double amount, String depositName) {
        return depositDAO.openDeposit(accountID, cardIdentifier, periods, amount, depositName);
    }

    @Override
    public void addAmountToDeposit(int depositID, double amount) throws SQLException {
        depositDAO.addAmountToDeposit(depositID, amount);
    }

    @Override
    public String getAccountUsername(int accountId) {
        return accountDao.getAccountUsername(accountId);
    }

    @Override
    public ArrayList<Deposit> getAllDeposits(int accountId) {
        return depositDAO.getAllDeposits(accountId);
    }
}
