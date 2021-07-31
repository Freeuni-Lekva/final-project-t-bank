package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.CardDAO;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import java.sql.Connection;
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

    @Override
    public List<CardType> getCardTypes() {
        return null;
    }

    @Override
    public List<CardInfo> getAccountCards(int accountId) {
        return null;
    }
}
