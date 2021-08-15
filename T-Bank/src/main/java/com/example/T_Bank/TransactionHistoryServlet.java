package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "TransactionsHistoryServlet", value = "/TransactionsHistoryServlet")
public class TransactionHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBankDAO = (TBankDAO) context.getAttribute("TBankDAO");
        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        int accountId = account.getAccountId();

        request.setAttribute("income", new ArrayList<Transaction>());
        request.setAttribute("expense", new ArrayList<Transaction>());
        request.setAttribute("currency", new ArrayList<Transaction>());

        request.setAttribute("currencyCheckBox", "false");
        request.setAttribute("incomeCheckBox", "false");
        request.setAttribute("expenseCheckBox", "false");

        String[] checkboxNamesList= request.getParameterValues("checkbox");

        if (checkboxNamesList != null) {
            for (String s : checkboxNamesList) {
                s = s.substring(8);
                switch (s) {
                    case "income":
                        ArrayList<Transaction> income = tBankDAO.getAllIncomes(accountId);
                        /*Transaction tr = new Transaction(111, 222, "aaa", "bbb", 1, null,1, 100);
                        income.add(tr);*/
                        request.setAttribute("income", income);
                        request.setAttribute("incomeCheckBox", "true");
                        break;
                    case "expense":
                        ArrayList<Transaction> expense = tBankDAO.getAllExpenses(accountId);
                        /*Transaction tr1 = new Transaction(111, 222, "aaa", "bbb", 1, null,1, 100);
                        expense.add(tr1);*/
                        request.setAttribute("expense", expense);
                        request.setAttribute("expenseCheckBox", "true");
                        break;
                    case "currency":
                        ArrayList<Transaction> currency = tBankDAO.getAllConversions(accountId);
                        /*Transaction tr2 = new Transaction(111, 222, "aaa", "bbb", 1, null,1, 100);
                        currency.add(tr2);
                        Transaction tr3 = new Transaction(11, 222, "aaa", "bbb", 1, null,1, 100);
                        currency.add(tr3);*/
                        request.setAttribute("currency", currency);
                        request.setAttribute("currencyCheckBox", "true");
                        break;
                }
            }
        }
        request.setAttribute("tBankDAO", tBankDAO);
        request.getRequestDispatcher("TransactionHistory.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
