package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
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
        List<CardInfo> rounded = new ArrayList<>();
        for (CardInfo card : cardInfos) {
            Double gel = Math.round(card.getGelBalance() * 100.0) / 100.0;
            Double usd = Math.round(card.getUsdBalance() * 100.0) / 100.0;
            Double eur = Math.round(card.getEuroBalance() * 100.0) / 100.0;
            CardInfo round = new CardInfo(card.getAccountCardId(), card.getCardIdentifier(), card.getAccountId(), card.getCardTypeId(), card.getCardName(),
                    gel, usd, eur, card.isValidCard(), card.getErrorMessage(), card.getCardType());
            rounded.add(round);
        }

        Double GEL = Math.round(cardInfos.stream().map(CardInfo::getGelBalance).reduce((a, b) -> a + b).get() * 100.0) / 100.0;
        Double USD = Math.round(cardInfos.stream().map(CardInfo::getUsdBalance).reduce((a, b) -> a + b).get() * 100.0) / 100.0;
        Double EUR = Math.round(cardInfos.stream().map(CardInfo::getEuroBalance).reduce((a, b) -> a + b).get() * 100.0) / 100.0;

        request.setAttribute("cards", rounded);
        request.setAttribute("GELsum", GEL);
        request.setAttribute("USDsum", USD);
        request.setAttribute("EURsum", EUR);

        request.getRequestDispatcher("CardsListPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
