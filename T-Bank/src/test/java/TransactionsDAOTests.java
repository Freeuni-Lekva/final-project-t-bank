import com.example.T_Bank.DAO.DatabaseConstants;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.AccountNumbersList;
import com.example.T_Bank.Storage.TransferError;
import org.apache.commons.dbcp2.BasicDataSource;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class TransactionsDAOTests {
    public static BasicDataSource dataSource;
    public static Connection connection;
    private TBankDAO tBank = new TBankDAO();
    private static Set<Integer> tmpAccIDs;
    private static Set<Integer> tmpCardIDs;
    private static String personalID;
    private static String cardID;
    private static String lastCardID;

    @BeforeClass
    public void setup() throws SQLException {
        getConnection();
        setupAccs();
        setupCards();
    }

    private void setupCards() throws SQLException {
        Set<Integer> startingCards = getRows("account_cards");
        addCards();
        Set<Integer> endingCards = getRows("account_cards");
        endingCards.removeAll(startingCards);
        tmpCardIDs = endingCards;
    }

    private void addCards() throws SQLException {
        String addCardQuery = "insert into t_bank_db.account_cards (account_id, card_identifier," +
                "card_type_id, card_name," +
                "gel_balance, usd_balance, euro_balance) values (?, ?, 1, 'testCard', 10000, 10000, 10000);";
        PreparedStatement statement = connection.prepareStatement(addCardQuery);

        for (int i : tmpAccIDs) {
            statement.setInt(1, i);
            statement.setString(2, "CardID" + i);
            if (cardID == null) {
                cardID = "CardID" + i;
            } else {
                lastCardID = "CardID" + i;
            }

            statement.executeUpdate();
        }
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

    private void setupAccs() throws SQLException {
        Set<Integer> startingIDs = getRows("accounts");
        addAccounts(2);
        Set<Integer> endingIDs = getRows("accounts");
        endingIDs.removeAll(startingIDs);
        tmpAccIDs = endingIDs;
    }

    private void addAccounts(int max) throws SQLException {
        String registerQuery = "insert into accounts " +
                " (first_name, last_name, personal_id," +
                " user_name, user_password, birth_date) " +
                "values ( ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(registerQuery);

        int startingIndex = getLastSeed();

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

    private Set<Integer> getRows(String table) throws SQLException {
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

    private void getConnection() throws SQLException {
        dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DatabaseConstants.databaseName);
        dataSource.setUsername(DatabaseConstants.databaseUsername);
        dataSource.setPassword(DatabaseConstants.databasePassword);
        connection = null;
        connection = dataSource.getConnection();
    }

    @Test
    public void testTransferMoney() {
        SoftAssert softAssert = new SoftAssert();
        TransferError transferError;

        transferError = tBank.transferMoney("none", cardID, 100, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(TransferError.accountNumberDoesNotExist, transferError);

        transferError = tBank.transferMoney(cardID, "none", 100, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(TransferError.accountNumberDoesNotExist, transferError);

        transferError = tBank.transferMoney(cardID, lastCardID, 20000, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(TransferError.notEnoughAmount, transferError);

        transferError = tBank.transferMoney(cardID, lastCardID, 100, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(TransferError.noErrorMessage, transferError);

        transferError = tBank.transferMoney(cardID, lastCardID, 100, tBank.getCurrencies().get(1), tBank.getCurrencies().get(2));
        softAssert.assertEquals(TransferError.noErrorMessage, transferError);

        softAssert.assertAll();
    }

    @Test
    public void testGetAccountNumbers() {
        SoftAssert softAssert = new SoftAssert();
        AccountNumbersList accountNumbersList;

        accountNumbersList = tBank.getAccountNumbers("none");
        softAssert.assertFalse(accountNumbersList.isValid());
        softAssert.assertEquals(TransferError.personalIdDoesNotExist, accountNumbersList.getErrorMessage());
        softAssert.assertNull(accountNumbersList.getAccountNumbers());

        accountNumbersList = tBank.getAccountNumbers(personalID);
        softAssert.assertTrue(accountNumbersList.isValid());
        softAssert.assertEquals(TransferError.noErrorMessage, accountNumbersList.getErrorMessage());
        softAssert.assertEquals(1, accountNumbersList.getAccountNumbers().size());

        softAssert.assertAll();
    }

    @AfterClass
    public void cleanup() throws SQLException {
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
