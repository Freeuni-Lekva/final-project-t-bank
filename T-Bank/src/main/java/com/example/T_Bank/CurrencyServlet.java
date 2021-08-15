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

@WebServlet(name = "CurrencyServlet", value = "/CurrencyServlet")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context=getServletContext();
        TBankDAO tBankDAO = (TBankDAO) context.getAttribute("TBankDAO");
        ArrayList<Currency> currencies=tBankDAO.getCurrencies();
        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");

        Account account = sessions.get(request.getSession().getId());
        if(account==null) {
            request.getRequestDispatcher("SessionExpiredPage.jsp").forward(request,response);
            return;
        }
        AccountNumbersList list=tBankDAO.getAccountNumbers(account.getPersonalId());
        ArrayList<String> cardIDs=list.getAccountNumbers();


        request.setAttribute("CardID",cardIDs);
        request.setAttribute("Currencies",currencies);
        request.getRequestDispatcher("CurrencyExchangePage.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBankDAO = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        if(account==null) {
            request.getRequestDispatcher("SessionExpiredPage.jsp").forward(request,response);
            return;
        }
        AccountNumbersList list=tBankDAO.getAccountNumbers(account.getPersonalId());
        ArrayList<String> cardIDs=list.getAccountNumbers();

        String fromString=request.getParameter("from");
        String toString=request.getParameter("to");
        String valueString=request.getParameter("amount");

        if(fromString==null || toString==null || valueString==null) {
            ArrayList<Currency> currencies=tBankDAO.getCurrencies();
            request.setAttribute("Currencies",currencies);
            request.setAttribute("CardID",cardIDs);
            request.setAttribute("error","Please fill in all the Fields");
            request.getRequestDispatcher("CurrencyExchangePage.jsp").forward(request,response);
            return;
        }

        int from= Integer.parseInt(fromString);
        int to= Integer.parseInt(toString);
        int value= Integer.parseInt(valueString);
        ArrayList<Currency> currencies=tBankDAO.getCurrencies();
        request.setAttribute("Currencies",currencies);
        request.setAttribute("CardID",cardIDs);

        if(request.getParameter("calculate")!=null) {
            double exchangeValue = tBankDAO.getExchangeValue(value, currencies.get(from), currencies.get(to));
            request.setAttribute("Amount",Math.round(value * 100.0) / 100.0 +" " + currencies.get(from).getCurrencyName() + " can be converted into");
            request.setAttribute("to",Math.round(exchangeValue * 100.0) / 100.0 + " " + currencies.get(to).getCurrencyName()+ ".");
            request.getRequestDispatcher("CurrencyExchangePage.jsp").forward(request,response);
        }

        if(request.getParameter("transfer")!=null) {
            int index= Integer.parseInt(request.getParameter("cards"));
            String cardIdentifier=cardIDs.get(index);
            System.out.println(cardIdentifier);
            TransferError error = tBankDAO.currencyExchange(cardIdentifier, value, currencies.get(from), currencies.get(to));
            if (error == TransferError.noErrorMessage) {
                request.setAttribute("error", "Transfer Successful");
            } else {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("CurrencyExchangePage.jsp").forward(request, response);
        }
    }
}
