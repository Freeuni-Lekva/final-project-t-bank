import com.example.T_Bank.DAO.DatabaseConstants;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.EventError;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;
import org.junit.runner.RunWith;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@RunWith(DataProviderRunner.class)
public class CrowdFundingDAOTests {
    public static BasicDataSource dataSource;
    public static Connection connection;
    private static Set<Integer> tmpAccIDs;
    private static Set<Integer> tmpCardIDs;
    private TBankDAO tBank = new TBankDAO();
    private static int lastTestID;

    @BeforeClass
    public static void setup() throws SQLException {
        getConnection();
        setupAccs();
        setupCards();
    }

    private static int getLastSeed() {
        String query = "select count(*) from testing_seeds";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            String addQuery = "insert into testing_seeds (test_seed) " +
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

    private static void setupCards() throws SQLException {
        Set<Integer> startingCards = getRows("account_cards");
        addCards();
        Set<Integer> endingCards = getRows("account_cards");
        for (int i : startingCards) {
            endingCards.remove(i);
        }
        tmpCardIDs = endingCards;
    }

    private static void setupAccs() throws SQLException {
        Set<Integer> startingIDs = getRows("accounts");
        addAccounts(2);
        Set<Integer> endingIDs = getRows("accounts");
        for (int i : startingIDs) {
            endingIDs.remove(i);
        }
        tmpAccIDs = endingIDs;
    }

    private static void addCards() throws SQLException {
        String addCardQuery = "insert into t_bank_db.account_cards (account_id, card_identifier," +
                "card_type_id, card_name," +
                "gel_balance, usd_balance, euro_balance) values (?, ?, 1, 'testCard', 10000, 10000, 10000);";
        PreparedStatement statement = connection.prepareStatement(addCardQuery);

        for (int i : tmpAccIDs) {
            statement.setInt(1, i);
            statement.setString(2, "CardID" + i);

            statement.executeUpdate();
        }
    }

    private static Set<Integer> getRows(String table) throws SQLException {
        Set<Integer> res = new HashSet<>();
        String getQuery = "SELECT * FROM t_bank_db." + table + ";";

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(getQuery);

        while (rs.next()) {
            int accountID = rs.getInt("account_id");
            res.add(accountID);
        }

        return res;
    }

    private static void addAccounts(int max) throws SQLException {
        String registerQuery = "insert into accounts " +
                " (first_name, last_name, personal_id," +
                " user_name, user_password, birth_date) " +
                "values ( ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(registerQuery);

        int startingIndex = getLastSeed();
        lastTestID = startingIndex;

        for (int i = startingIndex; i <= startingIndex + max; i++) {
            statement.setString(1, "testName" + i);
            statement.setString(2, "testSurname" + i);
            statement.setString(3, "testID" + i);
            statement.setString(4, "testUsername" + i);
            statement.setString(5, "testPass" + i);
            statement.setString(6, null);

            statement.executeUpdate();
        }
    }

    private static void getConnection() throws SQLException {
        dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DatabaseConstants.databaseName);
        dataSource.setUsername(DatabaseConstants.databaseUsername);
        dataSource.setPassword(DatabaseConstants.databasePassword);
        connection = null;
        connection = dataSource.getConnection();
    }

    @Test
    public void testCreateCrowdFundingEvent() {
        EventError eventError = tBank.createCrowdFundingEvent("e" + lastTestID, lastTestID, "CardID" + lastTestID,
                "description", 100, tBank.getCurrencies().get(0));
        Assert.assertEquals(EventError.noErrorMessage, eventError);

        eventError = tBank.createCrowdFundingEvent("evnt" + lastTestID, lastTestID, "CardID" + lastTestID,
                "description", -1, tBank.getCurrencies().get(0));
        Assert.assertEquals(EventError.targetMoneyLessThanZero, eventError);

        eventError = tBank.createCrowdFundingEvent("e" + lastTestID, lastTestID, "CardID" + lastTestID,
                "description", 100, tBank.getCurrencies().get(0));
        Assert.assertEquals(EventError.sameEventNameOnThisAccount, eventError);
    }

    @Test
    public void testDeleteCrowdFundingEvent() {

    }

    @Test
    public void testChangeEventTarget() {

    }

    @Test
    public void testGetPublicCrowdFundingEvents() {

    }

    @Test
    public void testGetSpecificEvents() {

    }

    @Test
    public void testSendFunds() {

    }

    @AfterClass
    public static void cleanup() throws SQLException {
        removeTmps("account_card", tmpCardIDs);
        removeTmps("account", tmpAccIDs);
    }

    private static void removeTmps(String table, Set<Integer> set) throws SQLException {
        for (int i : set) {
            String removeQuery = "delete from t_bank_db." + table + "s where " + table + "_id = " + i;
            Statement st = connection.createStatement();
            st.executeUpdate(removeQuery);
        }
    }
}

