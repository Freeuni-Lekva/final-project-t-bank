package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.CrowdFundingEventDAO;
import com.example.T_Bank.DAO.DAOInterfaces.CurrencyDAO;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;
import com.example.T_Bank.Storage.Currency;

import javax.naming.ldap.PagedResultsControl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CrowdFundingEventDAOImplementation implements CrowdFundingEventDAO {
    private CurrencyDAO currency;
    private Connection connection;

    public CrowdFundingEventDAOImplementation(Connection connection, CurrencyDAO currencyDAO) {
        this.connection = connection;
        this.currency = currencyDAO;
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

            if (targetMoney <= 0) {
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

    private Currency findSpecificCurrency(int currency_id) {
        String checkQuery = "select * from currency_exchange where currency_id = ?";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkQuery);
            checkStm.setInt(1, currency_id);
            ResultSet availableCurrency = checkStm.executeQuery();

            availableCurrency.next();
            Currency curr = new Currency(availableCurrency.getString("currency_name"),
                    availableCurrency.getInt("currency_id"),
                    availableCurrency.getDouble("call_price"),
                    availableCurrency.getDouble("bid_price"));
            return curr;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<CrowdFundingEvent> getPublicCrowdFundingEvents() {
        ArrayList<CrowdFundingEvent> allEvents = new ArrayList<>();

        String checkQuery = "select * from crowd_funding_events";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkQuery);
            ResultSet availableEvents = checkStm.executeQuery();

            while (availableEvents.next()) {
                int event_id = availableEvents.getInt("event_id");
                String event_name = availableEvents.getString("event_name");
                int account_id = availableEvents.getInt("account_id");
                String card_identifier = availableEvents.getString("card_identifier");
                String event_desc = availableEvents.getString("event_desc");
                double target = availableEvents.getDouble("target");
                double done = availableEvents.getDouble("done");
                boolean active_event = availableEvents.getBoolean("active_event");
                int currency_id = availableEvents.getInt("currency_id");

                Currency curr = findSpecificCurrency(currency_id);
                CrowdFundingEvent event = new CrowdFundingEvent(event_id, event_name, account_id, card_identifier, event_desc, target,
                        done, active_event, curr);
                allEvents.add(event);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allEvents;
    }

    private EventList findAllEvents(int accountId) {
        ArrayList<CrowdFundingEvent> allEvents = new ArrayList<>();

        String checkQuery = "select * from crowd_funding_events where account_id = ?";

        try {
            PreparedStatement checkStm = connection.prepareStatement(checkQuery);
            checkStm.setInt(1, accountId);
            ResultSet availableEvents = checkStm.executeQuery();

            int count = 0;

            while (availableEvents.next()) {
                count++;
                int event_id = availableEvents.getInt("event_id");
                String event_name = availableEvents.getString("event_name");
                int account_id = availableEvents.getInt("account_id");
                String card_identifier = availableEvents.getString("card_identifier");
                String event_desc = availableEvents.getString("event_desc");
                double target = availableEvents.getDouble("target");
                double done = availableEvents.getDouble("done");
                boolean active_event = availableEvents.getBoolean("active_event");
                int currency_id = availableEvents.getInt("currency_id");

                Currency curr = findSpecificCurrency(currency_id);
                CrowdFundingEvent event = new CrowdFundingEvent(event_id, event_name, account_id, card_identifier, event_desc, target,
                        done, active_event, curr);
                allEvents.add(event);
            }

            if (count == 0) {
                EventList currEvent = new EventList(null, EventError.noEventFound, false);
                return currEvent;
            }
            EventList currEvent = new EventList(allEvents, EventError.noErrorMessage, true);
            return currEvent;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        EventList currEvent = new EventList(null, EventError.noEventFound, false);
        return currEvent;
    }

    @Override
    public EventList getSpecificEvents(String personalNumber) {
        ArrayList<CrowdFundingEvent> allEvents = new ArrayList<>();

        String checkQuery = "select * from accounts where personal_id = ?";

        try {
            PreparedStatement checkStm = connection.prepareStatement(checkQuery);
            checkStm.setString(1, personalNumber);
            ResultSet availableAccounts = checkStm.executeQuery();

            int count = 0;

            while (availableAccounts.next()) {
                count++;
                int accountId = availableAccounts.getInt("account_id");
                EventList getSpecificEvents = findAllEvents(accountId);

                ArrayList<CrowdFundingEvent> currEvents = getSpecificEvents.getAllEvents();
                for (int i = 0; i < currEvents.size(); i++) {
                    allEvents.add(currEvents.get(i));
                }
            }

            if (count == 0) {
                EventList currEvent = new EventList(null, EventError.noAccountFound, false);
                return currEvent;
            }

            EventList currEvent = new EventList(allEvents, EventError.noErrorMessage, true);
            return currEvent;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        EventList currEvent = new EventList(null, EventError.noAccountFound, false);
        return currEvent;
    }

    public EventError sendFunds(int eventId, String fromCardIdentifier, double amount, Currency fromCurrency) {
        ArrayList<CrowdFundingEvent> allEvents = new ArrayList<>();

        String checkEventQuery = "select * from crowd_funding_events where event_id = ?";
        String checkCardQuery = "select * from account_cards where card_identifier = ?";

        String specificCurrencyBalance = fromCurrency.getCurrencyName().toLowerCase() + "_balance";

        try {
            PreparedStatement checkEventStm = connection.prepareStatement(checkEventQuery);
            PreparedStatement checkCardStm = connection.prepareStatement(checkCardQuery);
            checkEventStm.setInt(1, eventId);
            checkCardStm.setString(1,fromCardIdentifier);

            ResultSet specificEvent = checkEventStm.executeQuery();
            ResultSet specificCard = checkCardStm.executeQuery();

            int count = 0;

            while (specificEvent.next()) {
                if (!specificEvent.getBoolean("active_event")) {
                    return EventError.eventNotActive;
                }
                count ++;
                if (!specificCard.next()) {
                    return EventError.noCardFound;
                }

                double amountOnCard = specificCard.getDouble(specificCurrencyBalance);

                if (amountOnCard < amount) {
                    return EventError.notEnoughAmount;
                }

                int eventCurrency = specificEvent.getInt("currency_id");
                double eventDoneAmount = specificEvent.getDouble("done");
                double eventTargetAmount = specificEvent.getDouble("target");

                String checkQuery = "select * from currency_exchange where currency_id = ? ";
                double value;

                try {
                    PreparedStatement checkStm = connection.prepareStatement(checkQuery);
                    checkStm.setInt(1, eventCurrency);
                    ResultSet result = checkStm.executeQuery();
                    result.next();
                    Currency toCurrency = new Currency(result.getString("currency_name"),
                                                result.getInt("currency_id"),
                                                result.getDouble("call_price"),
                                                result.getDouble("bid_price"));

                    value = currency.getExchangeValue(amount, fromCurrency, toCurrency);

                    String updateQuery = "Update crowd_funding_events set done = ? where event_id = ?";
                    String updateCardQuery0 = "Update account_cards set ";
                    String updateCardQuery1 = " = ? where card_identifier = ?";
                    String updateCardQuery = updateCardQuery0 + specificCurrencyBalance + updateCardQuery1;
                    try {
                        PreparedStatement updateStm = connection.prepareStatement(updateQuery);
                        PreparedStatement updateCardStm = connection.prepareStatement(updateCardQuery);
                        updateStm.setDouble(1, eventDoneAmount + value);
                        updateStm.setInt(2, eventId);
                        updateCardStm.setDouble(1, amountOnCard - amount);
                        updateCardStm.setString(2, fromCardIdentifier);
                        updateCardStm.executeUpdate();
                        updateStm.executeUpdate();
                        if (eventTargetAmount <= eventDoneAmount + value) {
                            closeEvent(eventId);
                        }
                        return EventError.noErrorMessage;
                    }  catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (count == 0) {
                return EventError.noEventFound;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
