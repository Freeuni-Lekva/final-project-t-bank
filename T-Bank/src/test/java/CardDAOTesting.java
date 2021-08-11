import com.example.T_Bank.DAO.DatabaseConstants;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardErrorMessage;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDAOTesting {

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
    public void severalCardsTest() throws SQLException {
        TBankDAO dao = new TBankDAO();
        String test = "test++";
        String firstName = test;
        String lastName = test;
        String personalId = test;
        String userName = test;
        String password = test;
        int i = 0;
        Account account = null;
        while(true){
            account = dao.register(firstName + i, lastName + i, personalId + i,
                    userName + i, password + i, null);
            if(account.isValidAccount()){
                break;
            }
            i++;
        }
        List<CardInfo> cards = dao.getAccountCards(account.getAccountId());
        Assert.assertEquals(cards.size(), 1);
        CardInfo nextCard = dao.addCard(account.getAccountId(), dao.getCardTypes().get(0), cards.get(0).getCardName());
        Assert.assertFalse(nextCard.isValidCard());
        Assert.assertEquals(nextCard.getErrorMessage(), CardErrorMessage.CardWithSimilarNameAlreadyExists);
        nextCard = dao.addCard(account.getAccountId(), dao.getCardTypes().get(1),
                cards.get(0).getCardName() + "other");
        Assert.assertEquals(nextCard.getErrorMessage(), CardErrorMessage.NoErrorMessage);
        Assert.assertTrue(nextCard.isValidCard());
        cards = dao.getAccountCards(account.getAccountId());
        Assert.assertEquals(cards.size(), 2);
        int m = 15;
        for(int j = 0; j < m; j++){
            dao.addCard(account.getAccountId(), dao.getCardTypes().get(0), "some card " + j);
        }
        cards = dao.getAccountCards(account.getAccountId());
        Assert.assertEquals(cards.size(), m + 2);

        String cleanCardsQuery = "delete from account_cards where account_id = ?";
        String cleanAccountsQuery = "delete from accounts where account_id = ?";
        PreparedStatement stm = connection.prepareStatement(cleanCardsQuery);
        stm.setInt(1, account.getAccountId());
        stm.executeUpdate();
        stm = connection.prepareStatement(cleanAccountsQuery);
        stm.setInt(1, account.getAccountId());
        stm.executeUpdate();
        String checkQuery = "select count(*) from account_cards where account_id = ?";
        stm = connection.prepareStatement(checkQuery);
        stm.setInt(1, account.getAccountId());
        ResultSet rs = stm.executeQuery();
        rs.next();
        Assert.assertEquals(rs.getInt(1), 0);
        checkQuery = "select count(*) from accounts where account_id = ?";
        stm = connection.prepareStatement(checkQuery);
        stm.setInt(1, account.getAccountId());
        rs = stm.executeQuery();
        rs.next();
        Assert.assertEquals(rs.getInt(1), 0);
    }

    @Test
    public void standardCardAddCardTest() throws SQLException {
        TBankDAO dao = new TBankDAO();
        String test = "test--";
        String firstName = test;
        String lastName = test;
        String personalId = test;
        String userName = test;
        String password = test;
        List <Integer> registerIds = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            Account newAccount = dao.register(firstName + i, lastName + i,
                    personalId + i, userName + i, password + i,
                    null);
            if(newAccount.isValidAccount()){
                registerIds.add(newAccount.getAccountId());
            }
        }

        for(int i = 0; i < registerIds.size(); i++){
            int accountId = registerIds.get(i);
            List<CardInfo> cards = dao.getAccountCards(accountId);
            Assert.assertTrue(cards.size() == 1);
            CardInfo nextCard = cards.get(0);
            Assert.assertEquals(nextCard.getAccountId(), accountId);
        }
        List<CardType> cardTypes = dao.getCardTypes();
        for(int i = 0; i < registerIds.size(); i++){
            int accountId = registerIds.get(i);
            dao.addCard(accountId, cardTypes.get(0), "test_name");
            List<CardInfo> cards = dao.getAccountCards(accountId);
            Assert.assertEquals(cards.size(), 2);
        }
        String cleanCardsQuery = "delete from account_cards where account_id = ?";
        String cleanAccountsQuery = "delete from accounts where account_id = ?";
        PreparedStatement accountsStm = connection.prepareStatement(cleanAccountsQuery);
        PreparedStatement cardsStm = connection.prepareStatement(cleanCardsQuery);
        for(int i = 0; i < registerIds.size(); i++){
            cardsStm.setInt(1, registerIds.get(i));
            accountsStm.setInt(1, registerIds.get(i));
            cardsStm.executeUpdate();
            accountsStm.executeUpdate();
        }
        String checkQuery = "select count(*) from accounts where account_id = ?";
        PreparedStatement stm = connection.prepareStatement(checkQuery);
        for(int i = 0; i < registerIds.size(); i++){
            int accountId = registerIds.get(i);
            stm.setInt(1, accountId);
            ResultSet rs = stm.executeQuery();
            rs.next();
            Assert.assertEquals(rs.getInt(1), 0);
        }

    }



}
