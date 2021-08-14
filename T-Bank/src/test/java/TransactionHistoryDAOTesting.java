import com.example.T_Bank.DAO.DAOInterfaces.*;
import com.example.T_Bank.DAO.DatabaseConstants;
import com.example.T_Bank.DAO.Implementations.*;
import com.example.T_Bank.Storage.Account;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.testng.annotations.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TransactionHistoryDAOTesting extends BaseTest {
    public static BasicDataSource dataSource;
    public static Connection connection;
    private AccountDAO accountDao;
    private CardDAO cardDao;
    private TransactionsDAO transactionsDAO;
    private CurrencyDAO currencyDAO;
    private TransactionHistoryDAO transactionHistoryDAO;

    private Account account1;
    private Account account2;
    private Account account3;
    @BeforeClass
    public void testPreparation() throws SQLException, FileNotFoundException {
        connection=getConnection();
        resetDB();
        transactionHistoryDAO = new TransactionHistoryDAOImplementation(connection);
        accountDao = new AccountDAOImplementation(connection);
        cardDao = new CardDAOImplementation(connection);
        currencyDAO = new CurrencyDAOImplementation(connection, transactionHistoryDAO);
        transactionsDAO = new TransactionsDAOImplementation(connection, currencyDAO, transactionHistoryDAO);

        prepareAccountsAndCards();
    }


    @Test
    public void testGetAllTransactions() throws SQLException {
        int totalTransactionCount1=transactionHistoryDAO.getAllTransactions(1).size();
        Assert.assertEquals(6,totalTransactionCount1);

        int totalTransactionCount2=transactionHistoryDAO.getAllTransactions(2).size();
        Assert.assertEquals(9,totalTransactionCount2);

        int totalTransactionCount3=transactionHistoryDAO.getAllTransactions(3).size();
        Assert.assertEquals(7,totalTransactionCount3);
    }

    @Test
    public void testGetAllConversions() throws SQLException {
        int totalConversions1=transactionHistoryDAO.getAllConversions(1).size();
        Assert.assertEquals(2,totalConversions1);

        int totalConversions2=transactionHistoryDAO.getAllConversions(2).size();
        Assert.assertEquals(4,totalConversions2);

        int totalConversions3=transactionHistoryDAO.getAllConversions(3).size();
        Assert.assertEquals(2,totalConversions3);

    }

    @Test
    public void testGetAllIncomes() throws SQLException {
        int totalIncomes1=transactionHistoryDAO.getAllIncomes(1).size();
        Assert.assertEquals(2,totalIncomes1);

        int totalIncomes2=transactionHistoryDAO.getAllIncomes(2).size();
        Assert.assertEquals(2,totalIncomes2);

        int totalIncomes3=transactionHistoryDAO.getAllIncomes(3).size();
        Assert.assertEquals(3,totalIncomes3);
    }

    @Test
    public void testGetAllExpenses() throws SQLException {
        int totalExpenses1=transactionHistoryDAO.getAllExpenses(1).size();
        Assert.assertEquals(2,totalExpenses1);

        int totalExpenses2=transactionHistoryDAO.getAllExpenses(2).size();
        Assert.assertEquals(3,totalExpenses2);

        int totalExpenses3=transactionHistoryDAO.getAllExpenses(3).size();
        Assert.assertEquals(2,totalExpenses3);
    }

    @Test
    public void testGetAllMoneyTransfers() throws SQLException {
        int totalTransfers1=transactionHistoryDAO.getMoneyTransfers(1).size();
        Assert.assertEquals(4,totalTransfers1);

        int totalTransfers2=transactionHistoryDAO.getMoneyTransfers(2).size();
        Assert.assertEquals(5,totalTransfers2);

        int totalTransfers3=transactionHistoryDAO.getMoneyTransfers(3).size();
        Assert.assertEquals(5,totalTransfers3);
    }

    public void prepareAccountsAndCards() throws SQLException {
        account1=accountDao.register("account1","account1","11112222111","acc1","12345678",null);
        account2=accountDao.register("account2","account2","11112222222","acc2","12345678",null);
        account3=accountDao.register("account3","account3","11112222333","acc3","12345678",null);
        PreparedStatement statement1=connection.prepareStatement("insert into t_bank_test_db.account_cards (account_id, card_identifier," +
                "                card_type_id, card_name," +
                "                gel_balance, usd_balance, euro_balance) values (1, '1', 1, 'Mastercard1', 5000, 5000, 5000)");
        PreparedStatement statement2=connection.prepareStatement("insert into t_bank_test_db.account_cards (account_id, card_identifier," +
                "                card_type_id, card_name," +
                "                gel_balance, usd_balance, euro_balance) values (2, '2', 1, 'Mastercard2', 5000, 5000, 5000)");
        PreparedStatement statement3=connection.prepareStatement("insert into t_bank_test_db.account_cards (account_id, card_identifier," +
                "                card_type_id, card_name," +
                "                gel_balance, usd_balance, euro_balance) values (3, '3', 1, 'Mastercard3', 5000, 5000, 5000)");
        statement1.executeUpdate();
        statement2.executeUpdate();
        statement3.executeUpdate();

        currencyDAO.currencyExchange("1",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(1));
        currencyDAO.currencyExchange("2",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(1));
        currencyDAO.currencyExchange("3",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(1));
        currencyDAO.currencyExchange("2",400,currencyDAO.getCurrencies().get(1),currencyDAO.getCurrencies().get(2));

        transactionsDAO.transferMoney("1","2",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(0));
        transactionsDAO.transferMoney("2","1",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(0));
        transactionsDAO.transferMoney("1","3",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(0));
        transactionsDAO.transferMoney("3","1",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(0));
        transactionsDAO.transferMoney("3","2",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(0));
        transactionsDAO.transferMoney("2","3",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(0));
        transactionsDAO.transferMoney("2","3",100,currencyDAO.getCurrencies().get(0),currencyDAO.getCurrencies().get(0));
    }




}
