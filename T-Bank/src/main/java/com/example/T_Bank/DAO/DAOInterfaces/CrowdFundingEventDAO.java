package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.EventError;
import com.example.T_Bank.Storage.CrowdFundingEvent;
import com.example.T_Bank.Storage.EventList;

import java.util.ArrayList;

public interface CrowdFundingEventDAO {
    public EventError createCrowdFundingEvent(String eventName, int accountId, String cardIdentifier,
                                              String description, double targetMoney, Currency currency);
    public EventError deleteCrowdFundingEvent(int eventId);
    public EventError changeEventTarget(int eventId, double changedTarget);
    public ArrayList<CrowdFundingEvent> getPublicCrowdFundingEvents();
    public EventList getSpecificEvents(String personalNumber);
    public EventError sendFunds(int eventId, String fromCardIdentifier, double amount, Currency fromCurrency);
}
