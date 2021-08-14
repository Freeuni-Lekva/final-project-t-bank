package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LoanServlet", value = "/LoanServlet")
public class LoanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        AccountNumbersList cardsList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("Accounts", cardsList.getAccountNumbers());

        LoanList loanList = tBank.getAllLoans(account.getAccountId());
        List<Loan> loans = loanList.getLoans();
        request.setAttribute("Loans", loans);

        request.getRequestDispatcher("LoanPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        AccountNumbersList cardsList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("Accounts", cardsList.getAccountNumbers());

        int accountId = account.getAccountId();
        String cardIdentifier = cardsList.getAccountNumbers().get(Integer.parseInt(request.getParameter("accountDropdown")));
        double startMoney = Double.parseDouble(request.getParameter("amount"));
        int periods = Integer.parseInt(request.getParameter("period"));

        LoanErrorMessage loanErrorMessage = tBank.takeLoan(accountId, cardIdentifier, startMoney, periods);
        if (loanErrorMessage == LoanErrorMessage.noErrorMessage) {
            request.setAttribute("loanSuccess", "Loan Successful");
        } else {
            request.setAttribute("loanError", loanErrorMessage);
        }

        request.getRequestDispatcher("LoanPage.jsp").forward(request, response);
    }
}
