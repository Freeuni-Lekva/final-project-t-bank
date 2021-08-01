package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import java.util.List;

public interface CardDAO {
    public CardInfo addCard(int accountId, CardType cardType, String cardName);
    public List<CardType> getCardTypes();
    public List<CardInfo> getAccountCards(int accountId);
}
