package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.AccountNumbersList;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.Currency;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TransfersServlet", value = "/TransfersServlet")
public class TransfersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        String receiverID = request.getParameter("receiverID");
        AccountNumbersList receiverList = tBank.getAccountNumbers(receiverID);
        AccountNumbersList senderList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("senderAccounts", senderList.getAccountNumbers());

        if (receiverList.isValid() || receiverID == null) {
            List<String> accountCards = receiverList.getAccountNumbers();
            request.setAttribute("receiverAccounts", accountCards);
            request.setAttribute("receiverID", receiverID);
            request.getRequestDispatcher("TransfersPage.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", receiverList.getErrorMessage());
            request.getRequestDispatcher("TransfersPage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        ServletContext context = getServletContext();
//        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");
//
//        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
//        Account account = sessions.get(request.getSession().getId());
//
//        AccountNumbersList accountNumbersList = tBank.getAccountNumbers(request.getParameter("receiverID"));
//
//        if (accountNumbersList.isValid()) {
//            List<String> accountCards = accountNumbersList.getAccountNumbers();
//            request.setAttribute("accounts", accountCards);
//            request.getRequestDispatcher("TransfersPage.jsp").forward(request, response);
//        } else {
//            request.setAttribute("errorMessage", accountNumbersList.getErrorMessage());
//            request.getRequestDispatcher("TransfersPage.jsp").forward(request, response);
//        }

        String fromAccountNumber;
        String toAccountNumber;
        double amount;
        Currency fromCurrency;
        Currency toCurrency;

        request.getRequestDispatcher("TransfersPage.jsp").forward(request, response);
    }
}
