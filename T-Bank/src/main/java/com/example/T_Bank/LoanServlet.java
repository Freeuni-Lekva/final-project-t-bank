package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "LoanServlet", value = "/LoanServlet")
public class LoanServlet extends HttpServlet {
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

        AccountNumbersList cardsList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("Accounts", cardsList.getAccountNumbers());

        LoanList loanList = tBank.getAllLoans(account.getAccountId());
        List<Loan> loans = loanList.getLoans().stream().filter(Loan::isActiveLoan).collect(Collectors.toList());
        request.setAttribute("Loans", loans);

        request.getRequestDispatcher("LoanPage.jsp").forward(request, response);
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
        AccountNumbersList cardsList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("Accounts", cardsList.getAccountNumbers());

        int accountId = account.getAccountId();
        String cardIdentifier = cardsList.getAccountNumbers().get(Integer.parseInt(request.getParameter("accountDropdown")));
        String amount = request.getParameter("amount");
        String period = request.getParameter("period");
        if (amount == null || amount.equals("") || period == null || period.equals("")) {
            LoanList loanList = tBank.getAllLoans(account.getAccountId());
            List<Loan> loans = loanList.getLoans().stream().filter(Loan::isActiveLoan).collect(Collectors.toList());
            request.setAttribute("Loans", loans);
            request.setAttribute("loanError", "Amount and Period can't be Empty!");
            request.getRequestDispatcher("LoanPage.jsp").forward(request, response);
        } else {
            double startMoney = Double.parseDouble(amount);
            int periods = Integer.parseInt(period);

            LoanErrorMessage loanErrorMessage = tBank.takeLoan(accountId, cardIdentifier, startMoney, periods);
            if (loanErrorMessage == LoanErrorMessage.noErrorMessage) {
                request.setAttribute("loanSuccess", "Loan Successful");
            } else {
                request.setAttribute("loanError", loanErrorMessage);
            }

            LoanList loanList = tBank.getAllLoans(account.getAccountId());
            List<Loan> loans = loanList.getLoans().stream().filter(Loan::isActiveLoan).collect(Collectors.toList());
            request.setAttribute("Loans", loans);

            request.getRequestDispatcher("LoanPage.jsp").forward(request, response);
        }

    }
}
