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
import java.util.stream.Collectors;

@WebServlet(name = "DepositsServlet", value = "/DepositsServlet")
public class DepositServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        if (account == null) {
            request.getRequestDispatcher("SessionExpiredPage.jsp").forward(request,response);
            return;
        }

        AccountNumbersList cardsList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("Accounts", cardsList.getAccountNumbers());

        List<Deposit> deposits = tBank.getAllDeposits(account.getAccountId());
        request.setAttribute("Deposits", deposits);
        request.getRequestDispatcher("DepositPage.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        if (account == null) {
            request.getRequestDispatcher("SessionExpiredPage.jsp").forward(request,response);
            return;
        }
        AccountNumbersList cardsList = tBank.getAccountNumbers(account.getPersonalId());
        request.setAttribute("Accounts", cardsList.getAccountNumbers());
        if("delete".equals(request.getParameter("action"))){
            String depositID=request.getParameter("depositId");
            int id = Integer.parseInt(depositID);
            tBank.closeDeposit(id);
        } else {
            int accountId = account.getAccountId();
            String cardIdentifier = cardsList.getAccountNumbers().get(Integer.parseInt(request.getParameter("accountDropdown")));
            String amount = request.getParameter("amount");
            String periods = request.getParameter("periods");
            String name = request.getParameter("name");
            if (amount == null || amount.equals("") || periods == null || periods.equals("") || name == null || name.equals("")) {
                //  LoanList loanList = tBank.getAllLoans(account.getAccountId());
                List<Deposit> deposits = tBank.getAllDeposits(account.getAccountId());
                request.setAttribute("Deposits", deposits);
                request.setAttribute("depositError", "Amount, Period or Name can't be Empty!");
                request.getRequestDispatcher("DepositPage.jsp").forward(request, response);
                return;
            } else {
                double money = Double.parseDouble(amount);
                int period = Integer.parseInt(periods);

                DepositError ErrorMessage = tBank.openDeposit(accountId, cardIdentifier, period, money, name);
                if (ErrorMessage == ErrorMessage.noErrorMessage) {
                    request.setAttribute("success", "Deposit Successful");
                } else if (ErrorMessage == ErrorMessage.accountIdNotValid) {
                    request.setAttribute("depositError", ErrorMessage);
                } else if (ErrorMessage == ErrorMessage.cardNotValid) {
                    request.setAttribute("depositError", ErrorMessage);
                } else if (ErrorMessage == ErrorMessage.initialNegativeDeposit) {
                    request.setAttribute("depositError", ErrorMessage);
                } else if (ErrorMessage == ErrorMessage.periodsLessThanZero) {
                    request.setAttribute("depositError", ErrorMessage);
                } else if (ErrorMessage == ErrorMessage.notEnoughOnBalance) {
                    request.setAttribute("depositError", ErrorMessage);
                }
            }
        }
        List<Deposit> deposits = tBank.getAllDeposits(account.getAccountId());
        request.setAttribute("Deposits", deposits);
        request.getRequestDispatcher("DepositPage.jsp").forward(request, response);
    }
}
