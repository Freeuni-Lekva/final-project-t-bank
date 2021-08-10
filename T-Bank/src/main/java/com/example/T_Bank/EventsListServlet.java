package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "EventsListServlet", value = "/EventsListServlet")
public class EventsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        AccountNumbersList senderList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("senderAccounts", senderList.getAccountNumbers());

        String searchedID = request.getParameter("searchBar");

        if (searchedID == null || searchedID.equals("")) {
            List<CrowdFundingEvent> events = tBank.getPublicCrowdFundingEvents().stream().filter(crowdFundingEvent -> crowdFundingEvent.isActive()).collect(Collectors.toList());
            request.setAttribute("events", events);
        } else {
            EventList events = tBank.getSpecificEvents(searchedID);
            if (events.isValid()) {
                request.setAttribute("events", events.getAllEvents().stream().filter(crowdFundingEvent -> crowdFundingEvent.isActive()).collect(Collectors.toList()));
            } else {
                request.setAttribute("eventError", events.getErrorMessage());
            }
        }

        request.getRequestDispatcher("EventsListPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        List<CrowdFundingEvent> events = tBank.getPublicCrowdFundingEvents().stream().filter(crowdFundingEvent -> crowdFundingEvent.isActive()).collect(Collectors.toList());
        request.setAttribute("events", events);

        AccountNumbersList senderList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("senderAccounts", senderList.getAccountNumbers());

        String id = request.getParameter("eventID");
        if (id.equals("") || id == null) {
            request.setAttribute("transferError", "Please Choose an Event");
            request.getRequestDispatcher("EventsListPage.jsp").forward(request, response);
            return;
        }
        int eventId = Integer.parseInt(id);
        String fromAccount = senderList.getAccountNumbers().get(Integer.parseInt(request.getParameter("senderDropdown")));
        Currency currency = tBank.getCurrencies().get(Integer.parseInt(request.getParameter("fromCurrency")));
        double amount = 0.0;
        if (!request.getParameter("amount").equals("")) {
            amount = Double.parseDouble((request.getParameter("amount")));
        }

        if (amount <= 0) {
            request.setAttribute("transferError", "Amount should be more than 0");
        } else {
            EventError error = tBank.sendFunds(eventId, fromAccount, amount, currency);
            if (error.equals(EventError.noErrorMessage)) {
                request.setAttribute("transferSuccess", "Funding Successful");
            } else {
                request.setAttribute("transferError", error);
            }
        }


        request.getRequestDispatcher("EventsListPage.jsp").forward(request, response);
    }
}
