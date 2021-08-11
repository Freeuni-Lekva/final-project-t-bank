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
import java.util.*;

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

    @Test
    public void loginRegisterErrors() throws SQLException {
        int i = 0;
        TBankDAO dao = new TBankDAO();
        String test = "++++";
        String firstName = test;
        String lastName = test;
        String personalId = test;
        String userName = test;
        String password = test;
        Account newAccount = null;
        while(true){
            newAccount = dao.register(firstName + i, lastName + i, personalId + i,
                    userName + i, password + i, null);
            if(newAccount.isValidAccount()){
                break;
            }
            i++;
        }
        Assert.assertTrue(newAccount.isValidAccount());
        Assert.assertEquals(personalId + i, newAccount.getPersonalId());
        Assert.assertEquals(password + i, newAccount.getPassword());
        Assert.assertEquals(lastName + i, newAccount.getLastName());
        Assert.assertEquals(ErrorMessage.NoErrorMessage, newAccount.getErrorMessage());
        Assert.assertEquals(firstName + i, newAccount.getFirstName());

        Account badAccount = dao.login(userName + i, "bad password");
        Assert.assertEquals(ErrorMessage.AccountNotValid, badAccount.getErrorMessage());
        Assert.assertFalse(badAccount.isValidAccount());

        badAccount = dao.register("a", "a", "a", userName + i,
                "a", null);
        Assert.assertEquals(ErrorMessage.UserNameAlreadyExists, badAccount.getErrorMessage());
        badAccount = dao.register("a", "a", personalId + i, userName,
                password + i, null);
        Assert.assertEquals(ErrorMessage.PersonalIdAlreadyExists, badAccount.getErrorMessage());

        String cleanCards = "delete from account_cards where account_id = ?";
        String cleanAccounts = "delete from accounts where account_id = ?";
        PreparedStatement stm = connection.prepareStatement(cleanCards);
        stm.setInt(1, newAccount.getAccountId());
        stm.executeUpdate();
        stm = connection.prepareStatement(cleanAccounts);
        stm.setInt(1, newAccount.getAccountId());
        stm.executeUpdate();



    }

    @Test
    public void simpleLoginRegister() throws SQLException {
        TBankDAO dao = new TBankDAO();
        String test = "----";
        String firstName = test;
        String lastName = test;
        String personalId = test;
        String userName = test;
        String password = test;
        Map <Integer, Integer> registered = new HashMap<>();
        for(int i = 0; i < 100; i++){
            Account newAccount = dao.register(firstName + i, lastName + i, personalId + i,
                    userName + i, password + i, null);
            if(newAccount.isValidAccount()){
                registered.put(newAccount.getAccountId(), i);
            }
        }
        Iterator<Integer> it = registered.keySet().iterator();
        while(it.hasNext()){
            int nextId = it.next();
            int nextIndex = registered.get(nextId);
            Account account = dao.login(userName + nextIndex, password + nextIndex);
            Assert.assertTrue(account.isValidAccount());
            Assert.assertEquals(ErrorMessage.NoErrorMessage, account.getErrorMessage());
            Assert.assertEquals(account.getFirstName(), firstName + nextIndex);
            Assert.assertEquals(account.getLastName(), lastName + nextIndex);
            Assert.assertEquals(account.getPassword(), password + nextIndex);
            Assert.assertEquals(account.getPersonalId(), personalId + nextIndex);
        }
        String query = "delete from accounts where account_id = ?";
        String cardQuery = "delete from account_cards where account_id = ?";
        it = registered.keySet().iterator();
        while(it.hasNext()){
            int nextId = it.next();
            PreparedStatement stm = connection.prepareStatement(cardQuery);
            stm.setInt(1, nextId);
            stm.executeUpdate();
            stm = connection.prepareStatement(query);
            stm.setInt(1, nextId);
            stm.executeUpdate();
        }

    }



}
