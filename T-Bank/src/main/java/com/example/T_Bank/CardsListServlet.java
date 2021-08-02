package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.smartcardio.Card;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CardsListServlet", value = "/CardsListServlet")
public class CardsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");
        Account account = (Account) context.getAttribute("Account");
        int accountId = account.getAccountId();

        List<CardInfo> cardInfos = tBank.getAccountCards(accountId);
        request.setAttribute("cards", cardInfos);

        request.getRequestDispatcher("CardsListPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
