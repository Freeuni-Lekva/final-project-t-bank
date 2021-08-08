package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TransfersServlet", value = "/TransfersServlet")
public class TransfersServlet extends HttpServlet {
    private String receiverID = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        receiverID = request.getParameter("receiverID");
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
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        List<String> receiverList = tBank.getAccountNumbers(receiverID).getAccountNumbers();
        List<String> senderList = tBank.getAccountNumbers(account.getPersonalId()).getAccountNumbers();
        request.setAttribute("senderAccounts", senderList);
        String toAccount = request.getParameter("receiverDropdown");

        if (receiverID == null || receiverID.equals("") || toAccount == null || toAccount.equals("")) {
            request.setAttribute("transferError", "Target account cant be empty!");
            request.getRequestDispatcher("TransfersPage.jsp").forward(request, response);
        } else {
            String fromAccountNumber = senderList.get(Integer.parseInt(request.getParameter("senderDropdown")));
            String toAccountNumber = receiverList.get(Integer.parseInt(toAccount));
            double amount = 0.0;
            if (!request.getParameter("amount").equals("")) {
                amount = Double.parseDouble((request.getParameter("amount")));
            }
            if (amount <= 0){
                request.setAttribute("transferError", "Amount should be more than 0");
            }else {
                Currency fromCurrency = tBank.getCurrencies().get(Integer.parseInt(request.getParameter("fromCurrency")));
                Currency toCurrency = tBank.getCurrencies().get(Integer.parseInt(request.getParameter("toCurrency")));

                TransferError transferError = tBank.transferMoney(fromAccountNumber, toAccountNumber, amount, fromCurrency, toCurrency);
                if (transferError != TransferError.noErrorMessage) {
                    request.setAttribute("transferError", transferError);
                } else {
                    request.setAttribute("transferSuccess", "Transfer Successful!");
                }
            }
            request.getRequestDispatcher("TransfersPage.jsp").forward(request, response);
        }
    }
}
