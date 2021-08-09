package com.example.T_Bank.Storage;

import java.util.ArrayList;

public class EventList {
    private ArrayList<CrowdFundingEvent> allEvents;
    private EventError errorMessage;

    public EventList(ArrayList<CrowdFundingEvent> events, EventError errorMessage) {
        this.allEvents = events;
        this.errorMessage = errorMessage;
    }

    public EventError getErrorMessage() {
        return errorMessage;
    }

    public ArrayList<CrowdFundingEvent> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(ArrayList<CrowdFundingEvent> allEvents) {
        this.allEvents = allEvents;
    }

    public void setErrorMessage(EventError errorMessage) {
        this.errorMessage = errorMessage;
    }
}
