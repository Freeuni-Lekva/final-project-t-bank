package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.CrowdFundingEventDAO;
import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.EventError;

import javax.naming.ldap.PagedResultsControl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrowdFundingEventDAOImplementation implements CrowdFundingEventDAO {
    private Connection connection;

    public CrowdFundingEventDAOImplementation(Connection connection) {
        this.connection = connection;
    }

    @Override
    public EventError createCrowdFundingEvent(String eventName, int accountId,
                                              String cardIdentifier, String description,
                                              double targetMoney, Currency currency) {
        String query = "select event_name from crowd_funding_events where account_id = ? " +
                "and active_event = true";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, accountId);
            ResultSet rs = stm.executeQuery();
            List<String> eventNames = new ArrayList<>();

            while (rs.next()) {
                String nextEvent = rs.getString(1);
                eventNames.add(nextEvent);
            }

            for (int i = 0; i < eventNames.size(); i++) {
                if (eventNames.get(i).equals(eventName)) {
                    return EventError.sameEventNameOnThisAccount;
                }
            }

            if(targetMoney <= 0){
                return EventError.targetMoneyLessThanZero;
            }

            String insertQuery = "insert into crowd_funding_events (event_name, account_id, card_identifier," +
                    " event_desc, target, done, active_event, currency_id)" +
                    " values(?, ?, ?, ?, ?, ?, ?, ?) ";

            stm = connection.prepareStatement(insertQuery);
            stm.setString(1, eventName);
            stm.setInt(2, accountId);
            stm.setString(3, cardIdentifier);
            stm.setString(4, description);
            stm.setDouble(5, targetMoney);
            stm.setDouble(6, 0);
            stm.setBoolean(7, true);
            stm.setInt(8, currency.getCurrencyId());
            stm.executeUpdate();

            return EventError.noErrorMessage;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return EventError.noEventFound;
    }

    @Override
    public EventError deleteCrowdFundingEvent(int eventId) {
        String query = "select count(*) from crowd_funding_events where event_id = ? and active_event = true";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, eventId);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                return EventError.noEventFound;
            }

            String deleteQuery = "update crowd_funding_events " +
                    "set active_event = false " +
                    "where event_id = ?";

            stm = connection.prepareStatement(deleteQuery);
            stm.setInt(1, eventId);
            stm.executeUpdate();

            return EventError.noErrorMessage;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return EventError.noEventFound;
    }

    @Override
    public EventError changeEventTarget(int eventId, double changedTarget) {
        String checkQuery = "select count(*) from crowd_funding_events where event_id = ? and " +
                "active_event = true";

        try {
            PreparedStatement stm = connection.prepareStatement(checkQuery);
            stm.setInt(1, eventId);
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                return EventError.noEventFound;
            }

            String changeStatement = "update crowd_funding_events " +
                    "set target = ? " +
                    "where event_id = ?";

            stm = connection.prepareStatement(changeStatement);
            stm.setDouble(1, changedTarget);
            stm.setInt(2, eventId);
            stm.executeUpdate();

            checkQuery = "select done from crowd_funding_events " +
                    "where event_id = ? and active_event = true";

            stm = connection.prepareStatement(checkQuery);
            stm.setInt(1, eventId);
            rs = stm.executeQuery();
            rs.next();
            double done = rs.getDouble(1);

            if (done > changedTarget) {
                closeEvent(eventId);
            }

            return EventError.noErrorMessage;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return EventError.noEventFound;
    }

    private void closeEvent(int eventId) {
        String closeQuery = "update crowd_funding_events " +
                "set active_event = false " +
                "where event_id = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(closeQuery);

            stm.setInt(1, eventId);
            stm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
