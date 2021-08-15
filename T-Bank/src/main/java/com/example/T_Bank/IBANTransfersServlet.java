package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.AccountNumbersList;
import com.example.T_Bank.Storage.Currency;
import com.example.T_Bank.Storage.TransferError;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "IBANTransfersServlet", value = "/IBANTransfersServlet")
public class IBANTransfersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        if(account==null) {
            request.getRequestDispatcher("SessionExpiredPage.jsp").forward(request,response);
            return;
        }

        AccountNumbersList senderList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("senderAccounts", senderList.getAccountNumbers());

        request.getRequestDispatcher("IBANTransfersPage.jsp").forward(request, response);
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
        List<String> senderList = tBank.getAccountNumbers(account.getPersonalId()).getAccountNumbers();
        request.setAttribute("senderAccounts", senderList);
        String receiverIBAN = request.getParameter("receiverIBAN");

        if (receiverIBAN == null || receiverIBAN.equals("")) {
            request.setAttribute("transferError", "Target account cant be empty!");
            request.getRequestDispatcher("IBANTransfersPage.jsp").forward(request, response);
        } else {
            String fromAccountNumber = senderList.get(Integer.parseInt(request.getParameter("senderDropdown")));
            String toAccountNumber = request.getParameter("receiverIBAN");
            double amount = 0.0;
            if (!request.getParameter("amount").equals("")) {
                amount = Double.parseDouble((request.getParameter("amount")));
            }
            if (amount <= 0) {
                request.setAttribute("transferError", "Amount should be more than 0");
            } else {
                Currency fromCurrency = tBank.getCurrencies().get(Integer.parseInt(request.getParameter("fromCurrency")));
                Currency toCurrency = tBank.getCurrencies().get(Integer.parseInt(request.getParameter("toCurrency")));

                TransferError transferError = tBank.transferMoney(fromAccountNumber, toAccountNumber, amount, fromCurrency, toCurrency);
                if (transferError != TransferError.noErrorMessage) {
                    request.setAttribute("transferError", transferError);
                } else {
                    request.setAttribute("transferSuccess", "Transfer Successful!");
                }
            }
            request.getRequestDispatcher("IBANTransfersPage.jsp").forward(request, response);
        }
    }
}
