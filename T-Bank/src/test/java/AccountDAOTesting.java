import com.example.T_Bank.DAO.DatabaseConstants;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.ErrorMessage;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountDAOTesting {

    public static BasicDataSource dataSource;
    public static Connection connection;

    @BeforeAll
    public static void setUpDBConnection(){
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
    }
    private int getLastSeed(){
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

    @Test
    public void simpleLoginRegister(){
        TBankDAO dao = new TBankDAO();
        int testingSeed = getLastSeed();
        String test = "test";
        String firstName = test + testingSeed;
        String lastName = test + testingSeed;
        String personalId = test + testingSeed;
        String userName = test + testingSeed;
        String password = test + testingSeed;
        String birthdate = null;
        Account newAccount = dao.register(firstName, lastName, personalId,
                                          userName, password, birthdate);
        Assert.assertTrue(newAccount.isValidAccount());
        Assert.assertEquals(ErrorMessage.NoErrorMessage, newAccount.getErrorMessage());
        Assert.assertEquals(newAccount.getFirstName(), firstName);
        Assert.assertEquals(newAccount.getLastName(), lastName);
        Assert.assertEquals(newAccount.getPassword(), password);
        Assert.assertEquals(newAccount.getPersonalId(), personalId);
        Assert.assertEquals(newAccount.getUserName(), userName);

        Account notValidAccount = dao.register(firstName, lastName, personalId + "1",
                                               userName, password, birthdate);
        Assert.assertFalse(notValidAccount.isValidAccount());
        Assert.assertEquals(notValidAccount.getErrorMessage(), ErrorMessage.UserNameAlreadyExists);

        notValidAccount = dao.register(firstName, lastName, personalId,
                userName + "1", password, birthdate);

        Assert.assertFalse(notValidAccount.isValidAccount());
        Assert.assertEquals(notValidAccount.getErrorMessage(), ErrorMessage.PersonalIdAlreadyExists);

        List <CardInfo> cards = dao.getAccountCards(newAccount.getAccountId());
        Assert.assertEquals(1, cards.size());
        CardInfo standardCard = cards.get(0);

        Assert.assertEquals(standardCard.getAccountId(), newAccount.getAccountId());



        Account loginedAccount = dao.login(userName, "super wrong password");
        Assert.assertFalse(loginedAccount.isValidAccount());
        Assert.assertEquals(loginedAccount.getErrorMessage(), ErrorMessage.AccountNotValid);

        loginedAccount = dao.login(userName, password);
        Assert.assertTrue(loginedAccount.isValidAccount());
        Assert.assertEquals(loginedAccount.getErrorMessage(), ErrorMessage.NoErrorMessage);
        Assert.assertEquals(newAccount.getAccountId(), loginedAccount.getAccountId());
        Assert.assertEquals(newAccount.getUserName(), loginedAccount.getUserName());
        Assert.assertEquals(newAccount.getLastName(), loginedAccount.getLastName());
        Assert.assertEquals(newAccount.getPersonalId(), loginedAccount.getPersonalId());
    }


    @Test
    public void loginRegisterTest(){
        TBankDAO dao = new TBankDAO();
        int testingSeed = getLastSeed();
        String firstName = "f-----";
        String lastName  = "l----";
        String personalId = "p-id--";
        String userName = "us---";
        String password = "ps----";
        for(int i = 0; i < 10; i++){
            dao.register(firstName + testingSeed + i,lastName + testingSeed + i,
                    personalId + testingSeed + i, userName + testingSeed + i,
                    password + testingSeed + i, null);
        }

        for(int i = 0; i < 10; i++){
            Account account = dao.login(userName + testingSeed + i, password + testingSeed + i);
            Assert.assertTrue(account.isValidAccount());
            Assert.assertEquals(account.getErrorMessage(), ErrorMessage.NoErrorMessage);
            Assert.assertEquals(account.getFirstName(), firstName + testingSeed + i);
        }

    }
}
