import com.example.T_Bank.DAO.DAOInterfaces.*;
import com.example.T_Bank.DAO.DatabaseConstants;
import com.example.T_Bank.DAO.Implementations.*;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.CrowdFundingEvent;
import com.example.T_Bank.Storage.EventError;
import org.apache.commons.dbcp2.BasicDataSource;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CrowdFundingDAOTests {
    public BasicDataSource dataSource;
    public Connection connection;
    private Set<Integer> tmpAccIDs;
    private Set<Integer> tmpCardIDs;
    private Set<Integer> tmpEventIDs = new HashSet<>();
    private TBankDAO tBank = new TBankDAO();
    private int lastTestID;
    private String personalID;
    private String cardID;
    private AccountDAO accountDao;
    private TransactionsDAO transactionsDAO;
    private CurrencyDAO currencyDAO;
    private CrowdFundingEventDAO crowdFundingEventDAO;
    private TransactionHistoryDAO transactionHistoryDAO;
    private CardDAO cardDao;

    @BeforeClass
    public void setup() throws SQLException, FileNotFoundException {
        getConnection();
        resetDB();
        transactionHistoryDAO = new TransactionHistoryDAOImplementation(connection);
        accountDao = new AccountDAOImplementation(connection);
        cardDao = new CardDAOImplementation(connection);
        currencyDAO = new CurrencyDAOImplementation(connection, transactionHistoryDAO);
        transactionsDAO = new TransactionsDAOImplementation(connection, currencyDAO, transactionHistoryDAO);
        crowdFundingEventDAO = new CrowdFundingEventDAOImplementation(connection, currencyDAO);
        setupAccs();
        setupCards();
    }

    private void resetDB() throws FileNotFoundException, SQLException {
        InputStream in = new BufferedInputStream(new FileInputStream("create_test_db.sql"));
        Scanner scanner = new Scanner(in).useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");
        Statement statement = null;

        try {
            statement = connection.createStatement();

            while (scanner.hasNext()) {
                String line = scanner.next().trim();

                if (!line.isEmpty())
                    statement.execute(line);
            }
        } finally {
            if (statement != null)
                statement.close();
        }
    }

    private int getLastSeed() {
        String query = "select count(*) from t_bank_test_db.testing_seeds";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            String addQuery = "insert into t_bank_test_db.testing_seeds (test_seed) " +
                    "values(?)";
            stm = connection.prepareStatement(addQuery);
            stm.setInt(1, count + 1);
            stm.executeUpdate();
            return count + 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    private void setupCards() throws SQLException {
        Set<Integer> startingCards = getRows("account_cards");
        addCards();
        Set<Integer> endingCards = getRows("account_cards");
        endingCards.removeAll(startingCards);
        tmpCardIDs = endingCards;
    }

    private void setupAccs() throws SQLException {
        Set<Integer> startingIDs = getRows("accounts");
        addAccounts(2);
        Set<Integer> endingIDs = getRows("accounts");
        endingIDs.removeAll(startingIDs);
        tmpAccIDs = endingIDs;
    }

    private void addCards() throws SQLException {
        String addCardQuery = "insert into t_bank_test_db.account_cards (account_id, card_identifier," +
                "card_type_id, card_name," +
                "gel_balance, usd_balance, euro_balance) values (?, ?, 1, 'testCard', 10000, 10000, 10000);";
        PreparedStatement statement = connection.prepareStatement(addCardQuery);

        for (int i : tmpAccIDs) {
            statement.setInt(1, i);
            statement.setString(2, "CardID" + i);
            cardID = "CardID" + i;

            statement.executeUpdate();
        }
    }

    private Set<Integer> getRows(String table) throws SQLException {
        Set<Integer> res = new HashSet<>();
        String getQuery = "SELECT * FROM t_bank_test_db." + table + ";";

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getQuery);

        while (rs.next()) {
            int accountID = rs.getInt("account_id");
            res.add(accountID);
        }

        return res;
    }

    private void addAccounts(int max) throws SQLException {
        String registerQuery = "insert into t_bank_test_db.accounts " +
                " (first_name, last_name, personal_id," +
                " user_name, user_password, birth_date) " +
                "values ( ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(registerQuery);

        int startingIndex = getLastSeed();
        lastTestID = startingIndex;

        for (int i = startingIndex; i < startingIndex + max; i++) {
            statement.setString(1, "testName" + i);
            statement.setString(2, "testSurname" + i);
            statement.setString(3, "testID" + i);
            if (personalID == null) {
                personalID = "testID" + i;
            }
            statement.setString(4, "testUsername" + i);
            statement.setString(5, "testPass" + i);
            statement.setString(6, null);

            statement.executeUpdate();
        }
    }

    private void getConnection() throws SQLException {
        dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DatabaseConstants.testDBName);
        dataSource.setUsername(DatabaseConstants.databaseUsername);
        dataSource.setPassword(DatabaseConstants.databasePassword);
        connection = null;
        connection = dataSource.getConnection();
    }

    @Test(priority = 1)
    public void testCreateCrowdFundingEvent() {
        EventError eventError = crowdFundingEventDAO.createCrowdFundingEvent("e" + lastTestID, tmpAccIDs.iterator().next(), "CardID" + tmpCardIDs.iterator().next(),
                "description", 100, tBank.getCurrencies().get(0));
        tmpEventIDs.add(lastTestID);
        Assert.assertEquals(eventError, EventError.noErrorMessage);

        eventError = crowdFundingEventDAO.createCrowdFundingEvent("evnt" + lastTestID, lastTestID, "CardID" + lastTestID,
                "description", -1, tBank.getCurrencies().get(0));
        Assert.assertEquals(eventError, EventError.targetMoneyLessThanZero);

        eventError = crowdFundingEventDAO.createCrowdFundingEvent("e" + lastTestID, tmpAccIDs.iterator().next(), "CardID" + tmpCardIDs.iterator().next(),
                "description", 100, tBank.getCurrencies().get(0));
        Assert.assertEquals(eventError, EventError.sameEventNameOnThisAccount);
    }

    @Test(priority = 6)
    public void testDeleteCrowdFundingEvent() {
        EventError eventError = crowdFundingEventDAO.deleteCrowdFundingEvent(tmpEventIDs.iterator().next());
        Assert.assertEquals(eventError, EventError.noErrorMessage);
    }

    @Test(priority = 2)
    public void testChangeEventTarget() {
        EventError eventError = crowdFundingEventDAO.changeEventTarget(tmpEventIDs.iterator().next(), 300);
        Assert.assertEquals(eventError, EventError.noErrorMessage);
    }

    @Test(priority = 3)
    public void testGetPublicCrowdFundingEvents() {
        ArrayList<CrowdFundingEvent> events = crowdFundingEventDAO.getPublicCrowdFundingEvents();
        Assert.assertFalse(events.isEmpty());
    }

    @Test(priority = 4)
    public void testGetSpecificEvents() {
        ArrayList<CrowdFundingEvent> events = crowdFundingEventDAO.getSpecificEvents(personalID).getAllEvents();
        Assert.assertFalse(events.isEmpty());

        events = crowdFundingEventDAO.getSpecificEvents("none").getAllEvents();
        Assert.assertNull(events);
    }

    @Test(priority = 5)
    public void testSendFunds() {
        EventError eventError = crowdFundingEventDAO.sendFunds(tmpEventIDs.iterator().next(), cardID, 1, tBank.getCurrencies().get(0));
        Assert.assertEquals(eventError, EventError.noErrorMessage);
    }
}

