import com.example.T_Bank.DAO.DAOInterfaces.*;
import com.example.T_Bank.DAO.DatabaseConstants;
import com.example.T_Bank.DAO.Implementations.*;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.AccountNumbersList;
import com.example.T_Bank.Storage.TransferError;
import org.apache.commons.dbcp2.BasicDataSource;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Scanner;
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
    private AccountDAO accountDao;
    private CardDAO cardDao;
    private TransactionsDAO transactionsDAO;
    private CurrencyDAO currencyDAO;
    private TransactionHistoryDAO transactionHistoryDAO;

    @BeforeClass
    public void setup() throws SQLException, FileNotFoundException {
        getConnection();
        resetDB();
        transactionHistoryDAO = new TransactionHistoryDAOImplementation(connection);
        accountDao = new AccountDAOImplementation(connection);
        cardDao = new CardDAOImplementation(connection);
        currencyDAO = new CurrencyDAOImplementation(connection, transactionHistoryDAO);
        transactionsDAO = new TransactionsDAOImplementation(connection, currencyDAO, transactionHistoryDAO);
        setupAccs();
        setupCards();
    }

    private void resetDB() throws FileNotFoundException, SQLException {
        InputStream in = new BufferedInputStream(new FileInputStream("create_test_db.sql"));
        Scanner s = new Scanner(in).useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");
        Statement st = null;

        try {
            st = connection.createStatement();

            while (s.hasNext()) {
                String line = s.next().trim();

                if (!line.isEmpty())
                    st.execute(line);
            }
        } finally {
            if (st != null)
                st.close();
        }
    }

    private void setupCards() throws SQLException {
        Set<Integer> startingCards = getRows("account_cards");
        addCards();
        Set<Integer> endingCards = getRows("account_cards");
        endingCards.removeAll(startingCards);
        tmpCardIDs = endingCards;
    }

    private void addCards() throws SQLException {
        String addCardQuery = "insert into t_bank_test_db.account_cards (account_id, card_identifier," +
                "card_type_id, card_name," +
                "gel_balance, usd_balance, euro_balance) values (?, ?, 1, 'testCard', 100000, 10000, 10000);";
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

    private void setupAccs() throws SQLException {
        Set<Integer> startingIDs = getRows("accounts");
        addAccounts(2);
        Set<Integer> endingIDs = getRows("accounts");
        endingIDs.removeAll(startingIDs);
        tmpAccIDs = endingIDs;
    }

    private void addAccounts(int max) throws SQLException {
        String registerQuery = "insert into t_bank_test_db.accounts " +
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
        String getQuery = "SELECT * FROM t_bank_test_db." + table + ";";

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

        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DatabaseConstants.testDBName);
        dataSource.setUsername(DatabaseConstants.databaseUsername);
        dataSource.setPassword(DatabaseConstants.databasePassword);
        connection = null;
        connection = dataSource.getConnection();
    }

    @Test
    public void testTransferMoney() {
        SoftAssert softAssert = new SoftAssert();
        TransferError transferError;

        transferError = transactionsDAO.transferMoney("none", cardID, 100, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(transferError, TransferError.accountNumberDoesNotExist);

        transferError = transactionsDAO.transferMoney(cardID, "none", 100, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(transferError, TransferError.accountNumberDoesNotExist);

        transferError = transactionsDAO.transferMoney(cardID, lastCardID, 300000, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(transferError, TransferError.notEnoughAmount);

        transferError = transactionsDAO.transferMoney(cardID, lastCardID, 100, tBank.getCurrencies().get(0), tBank.getCurrencies().get(0));
        softAssert.assertEquals(transferError, TransferError.noErrorMessage);

        transferError = transactionsDAO.transferMoney(cardID, lastCardID, 100, tBank.getCurrencies().get(1), tBank.getCurrencies().get(2));
        softAssert.assertEquals(transferError, TransferError.noErrorMessage);

        softAssert.assertAll();
    }

    @Test
    public void testGetAccountNumbers() {
        SoftAssert softAssert = new SoftAssert();
        AccountNumbersList accountNumbersList;

        accountNumbersList = transactionsDAO.getAccountNumbers("none");
        softAssert.assertFalse(accountNumbersList.isValid());
        softAssert.assertEquals(accountNumbersList.getErrorMessage(), TransferError.personalIdDoesNotExist);
        softAssert.assertNull(accountNumbersList.getAccountNumbers());

        accountNumbersList = transactionsDAO.getAccountNumbers(personalID);
        softAssert.assertTrue(accountNumbersList.isValid());
        softAssert.assertEquals(accountNumbersList.getErrorMessage(), TransferError.noErrorMessage);
        softAssert.assertEquals(1, accountNumbersList.getAccountNumbers().size());

        softAssert.assertAll();
    }
}
