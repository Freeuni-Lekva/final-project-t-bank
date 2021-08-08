package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.EventError;

import java.util.ArrayList;

public interface CrowdFundingEventDAO {
    public EventError createCrowdFundingEvent(String eventName, int accountId, String cardIdentifier,
                                              String description, double targetMoney);
    public EventError deleteCrowdFundingEvent(int eventId);
    public EventError changeEventTarget(int eventId, double changedTarget);
    public ArrayList<EventError> getPublicCrowdFundingEvents();
    public ArrayList<EventError> getSpecificEvents(String personalNumber);

}
