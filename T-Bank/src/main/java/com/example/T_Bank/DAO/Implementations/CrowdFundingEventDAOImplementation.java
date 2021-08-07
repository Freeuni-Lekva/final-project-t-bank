package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.CrowdFundingEventDAO;
import com.example.T_Bank.Storage.EventError;

import java.sql.Connection;
import java.util.ArrayList;

public class CrowdFundingEventDAOImplementation implements CrowdFundingEventDAO {
    private Connection connection;
    public CrowdFundingEventDAOImplementation(Connection connection){
        this.connection = connection;
    }

    @Override
    public EventError createCrowdFundingEvent(String eventName, int accountId, String cardIdentifier, String description, double targetMoney) {
        return null;
    }

    @Override
    public EventError deleteCrowdFundingEvent(int eventId) {
        return null;
    }

    @Override
    public EventError changeEventTarget(int eventId, double changedTarget) {
        return null;
    }

    @Override
    public ArrayList<EventError> getPublicCrowdFundingEvents() {
        return null;
    }

    @Override
    public ArrayList<EventError> getSpecificEvents(String personalNumber) {
        return null;
    }
}
