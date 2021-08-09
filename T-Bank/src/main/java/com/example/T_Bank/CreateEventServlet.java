package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.EventError;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CreateEventServlet", value = "/CreateEventServlet")
public class CreateEventServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO TBankDAO = (TBankDAO) context.getAttribute("TBankDAO");
        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        String sessionId = request.getSession().getId();
        Account account = sessions.get(sessionId);
        List<String> cards = TBankDAO.getAccountNumbers(account.getPersonalId()).getAccountNumbers();
        List<Currency> currencies = TBankDAO.getCurrencies();

        fillDropDowns(request, cards, currencies);

        request.getRequestDispatcher("CreateEventPage.jsp").forward(request, response);
    }

    private void fillDropDowns(HttpServletRequest request, List<String> cards, List<Currency> currencies ) {
        request.setAttribute("cards", cards);
        request.setAttribute("currencies",currencies);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO TBankDAO = (TBankDAO) context.getAttribute("TBankDAO");
        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        String sessionId = request.getSession().getId();
        Account account = sessions.get(sessionId);
        List<Currency> currencies = TBankDAO.getCurrencies();
        List<String> cards = TBankDAO.getAccountNumbers(account.getPersonalId()).getAccountNumbers();

        fillDropDowns(request, cards, currencies);

        String eventName = request.getParameter("eventName");
        int accountId = account.getAccountId();
        String cardIdentifier = cards.get(Integer.parseInt(request.getParameter("targetCards")));
        String description = request.getParameter("description");
        double targetMoney = 0;
        if (!request.getParameter("target").equals("")) {
            targetMoney = Double.parseDouble(request.getParameter("target"));
        }
        if (eventName.equals("")){
            request.setAttribute("error", "Event should have a name");
            request.getRequestDispatcher("CreateEventPage.jsp").forward(request, response);
        }else if (description.equals("")){
            request.setAttribute("error", "Event should have a description");
            request.getRequestDispatcher("CreateEventPage.jsp").forward(request, response);
        }else {
            Currency currency = currencies.get(Integer.parseInt(request.getParameter("targetCurrencies")));


            EventError event = TBankDAO.createCrowdFundingEvent(eventName, accountId,
                    cardIdentifier, description,
                    targetMoney, currency);
            if (event.equals(EventError.noErrorMessage)) {
                request.setAttribute("success", "Event created");
                request.getRequestDispatcher("CreateEventPage.jsp").forward(request, response);
            } else {
                request.setAttribute("error", event);
                request.getRequestDispatcher("CreateEventPage.jsp").forward(request, response);

            }
        }
    }
}
