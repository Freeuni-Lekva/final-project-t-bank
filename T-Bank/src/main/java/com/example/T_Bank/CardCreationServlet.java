package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "CardCreationServlet", value = "/CardCreationServlet")
public class CardCreationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        List<CardType> types = tBank.getCardTypes();
        request.setAttribute("cardTypes", types);

        request.getRequestDispatcher("CardCreationPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        if(account==null) {
            request.getRequestDispatcher("SessionExpiredPage.jsp").forward(request,response);
            return;
        }

        List<CardType> types = tBank.getCardTypes();
        request.setAttribute("cardTypes", types);

        CardType cardType = types.get(Integer.parseInt(request.getParameter("dropdown")));
        String cardName = request.getParameter("cardName");
        int accountId = account.getAccountId();
        CardInfo cardInfo = tBank.addCard(accountId, cardType, cardName);
        if (cardName.length() == 0) {
            request.setAttribute("errorMessage", "Card name can't be empty");
            request.getRequestDispatcher("CardCreationPage.jsp").forward(request, response);
        } else if (cardName.length() > 30) {
            request.setAttribute("errorMessage", "Card name can't be longer than 30 chars");
            request.getRequestDispatcher("CardCreationPage.jsp").forward(request, response);
        } else if (cardInfo.isValidCard()) {
            request.setAttribute("successMessage", "Card added Successfully");
            request.getRequestDispatcher("CardCreationPage.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", cardInfo.getErrorMessage());
            request.getRequestDispatcher("CardCreationPage.jsp").forward(request, response);
        }
    }
}
