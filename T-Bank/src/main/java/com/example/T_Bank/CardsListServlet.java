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
import java.util.Map;

@WebServlet(name = "CardsListServlet", value = "/CardsListServlet")
public class CardsListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        int accountId = account.getAccountId();

        List<CardInfo> cardInfos = tBank.getAccountCards(accountId);
        Double GEL = cardInfos.stream().map(CardInfo::getGelBalance).reduce((a, b) -> a + b).get();
        Double USD = cardInfos.stream().map(CardInfo::getUsdBalance).reduce((a, b) -> a + b).get();
        Double EUR = cardInfos.stream().map(CardInfo::getEuroBalance).reduce((a, b) -> a + b).get();

        request.setAttribute("cards", cardInfos);
        request.setAttribute("GELsum", GEL);
        request.setAttribute("USDsum", USD);
        request.setAttribute("EURsum", EUR);

        request.getRequestDispatcher("CardsListPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
