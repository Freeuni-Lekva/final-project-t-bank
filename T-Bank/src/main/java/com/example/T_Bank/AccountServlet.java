package com.example.T_Bank;

import com.example.T_Bank.Storage.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "AccountServlet", value = "/AccountServlet")
public class AccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context=getServletContext();

        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());

        if(account==null) {
             request.getRequestDispatcher("SessionExpiredPage.jsp").forward(request,response);
             return;
        }

            request.setAttribute("FirstName", account.getFirstName());
            request.setAttribute("LastName", account.getLastName());
            request.setAttribute("UserName", account.getUserName());
            request.setAttribute("password", account.getPassword());
            request.setAttribute("PersonalID", account.getPersonalId());
            request.setAttribute("AccountID", account.getAccountId());

            request.getRequestDispatcher("AccountInfo.jsp").forward(request, response);



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
