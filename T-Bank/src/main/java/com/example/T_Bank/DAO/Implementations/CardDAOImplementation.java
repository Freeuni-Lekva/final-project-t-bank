package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.CardDAO;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAOImplementation implements CardDAO {
    private Connection connection;
    public CardDAOImplementation(Connection connection){
        this.connection = connection;
    }
    @Override
    public CardInfo addCard(int accountId, CardType cardType, String cardName) {
        return null;
    }


    private CardType getCardFromResultSet(ResultSet rs){
        CardType card = null;
        try {
            card =  new CardType(rs.getInt(1), rs.getString(2), rs.getInt(4),
                    rs.getString(3));
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
            while(resultSet.next()){
                cardTypes.add(getCardFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cardTypes;
    }

    @Override
    public List<CardInfo> getAccountCards(int accountId) {
        String accountCardsQuery = "select * from account_cards where account_id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(accountCardsQuery);
            stm.setInt(1, accountId);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
