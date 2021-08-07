package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.CardDAO;
import com.example.T_Bank.Storage.CardErrorMessage;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAOImplementation implements CardDAO {
    private Connection connection;

    public CardDAOImplementation(Connection connection) {
        this.connection = connection;
    }

    private List<String> getCardNames(ResultSet rs) {
        List<String> cardNames = new ArrayList<>();
        try {
            while (rs.next()) {
                String name = rs.getString(1);
                cardNames.add(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cardNames;
    }

    @Override
    public CardInfo addCard(int accountId, CardType cardType, String cardName) {
        String checkQuery = "select card_name from account_cards where account_id = ?";
        CardInfo card = null;
        try {
            PreparedStatement stm = connection.prepareStatement(checkQuery);
            stm.setInt(1, accountId);
            ResultSet rs = stm.executeQuery();
            List<String> cardNames = getCardNames(rs);
            long count = cardNames.stream().filter(c -> c.equals(cardName)).count();
            if (count > 0) {
                return new CardInfo(-1, null, -1, -1, null,
                        0, 0, 0, false,
                        CardErrorMessage.CardWithSimilarNameAlreadyExists, null);
            }

            String addCardQuery = "insert into account_cards (account_id, card_identifier, " +
                    "card_type_id, card_name," +
                    "gel_balance, usd_balance, euro_balance)" +
                    " values(?, ?, ?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(addCardQuery);
            stm.setInt(1, accountId);
            stm.setString(2, null);
            stm.setInt(3, cardType.getCardTypeId());
            stm.setString(4, cardName);
            stm.setDouble(5, 0);
            stm.setDouble(6, 0);
            stm.setDouble(7, 0);
            stm.executeUpdate();
            String getPrimaryKey = "select account_card_id from account_cards" +
                    " where account_id = ? and card_name = ?";
            stm = connection.prepareStatement(getPrimaryKey);
            stm.setInt(1, accountId);
            stm.setString(2, cardName);
            rs = stm.executeQuery();
            rs.next();
            int accountCardId = rs.getInt(1);

            String query = "update account_cards " +
                    "set card_identifier = ? " +
                    "where account_card_id = ?";

            String tmp = "";
            tmp += accountCardId;
            while (tmp.length() < 5) {
                tmp = "0" + tmp;
            }
            String cardIdentifier = "TB" + cardType.getCardPrefix() + tmp;

            stm = connection.prepareStatement(query);
            stm.setString(1, cardIdentifier);
            stm.setInt(2, accountCardId);
//            System.out.println(stm);
            stm.executeUpdate();


            card = new CardInfo(accountCardId, cardIdentifier, accountId, cardType.getCardTypeId(), cardName, 0, 0,
                    0, true, CardErrorMessage.NoErrorMessage, cardType);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return card;
    }


    private CardType getCardFromResultSet(ResultSet rs) {
        CardType card = null;
        try {
            card = new CardType(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getInt(5),
                    rs.getString(4));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return card;
    }

    @Override
    public List<CardType> getCardTypes() {
        String cardTypesQuery = "select * from card_types";
        List<CardType> cardTypes = new ArrayList<>();
        try {
            Statement stm = connection.createStatement();
            ResultSet resultSet = stm.executeQuery(cardTypesQuery);
            while (resultSet.next()) {
                cardTypes.add(getCardFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cardTypes;
    }

    private CardInfo getCardInfoFromResultSet(ResultSet rs) {
        CardInfo cardInfo = null;
        try {
            int accountCardId = rs.getInt(1);
            String cardIdentifier = rs.getString(2);
            int accountId = rs.getInt(3);
            int cardTypeId = rs.getInt(4);
            String cardName = rs.getString(5);
            double gelBalance = rs.getDouble(6);
            double usdBalance = rs.getDouble(7);
            double euroBalance = rs.getDouble(8);
            CardType cardType = getCardTypes().stream()
                    .filter(c -> c.getCardTypeId() == cardTypeId).findFirst().get();
            cardInfo = new CardInfo(accountCardId, cardIdentifier, accountId, cardTypeId, cardName,
                    gelBalance, usdBalance, euroBalance, true, CardErrorMessage.NoErrorMessage,
                    cardType);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cardInfo;
    }

    @Override
    public List<CardInfo> getAccountCards(int accountId) {
        String accountCardsQuery = "select * from account_cards where account_id = ?";
        List<CardInfo> cardInfos = new ArrayList<>();
        try {
            PreparedStatement stm = connection.prepareStatement(accountCardsQuery);
            stm.setInt(1, accountId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                cardInfos.add(getCardInfoFromResultSet(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cardInfos;
    }
}
