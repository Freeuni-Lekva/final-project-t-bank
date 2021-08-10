package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.AccountNumbersList;
import com.example.T_Bank.Storage.CrowdFundingEvent;
import com.example.T_Bank.Storage.EventList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
            ArrayList<CrowdFundingEvent> events = tBank.getPublicCrowdFundingEvents();
            request.setAttribute("events", events);
        } else {
            EventList events = tBank.getSpecificEvents(searchedID);
            if (events.isValid()) {
                request.setAttribute("events", events.getAllEvents());
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

        ArrayList<CrowdFundingEvent> events = tBank.getPublicCrowdFundingEvents();
        request.setAttribute("events", events);

        AccountNumbersList senderList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("senderAccounts", senderList.getAccountNumbers());

        request.getRequestDispatcher("EventsListPage.jsp").forward(request, response);
    }
}
