package com.example.T_Bank;

import com.example.T_Bank.Storage.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AccountServlet", value = "/AccountServlet")
public class AccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context=getServletContext();
        Account account= (Account) context.getAttribute("Account");
        request.setAttribute("FirstName",account.getFirstName());
        request.setAttribute("LastName",account.getLastName());
        request.setAttribute("UserName",account.getUserName());
        request.setAttribute("password",account.getPassword());
        request.setAttribute("PersonalID",account.getPersonalId());
        request.setAttribute("AccountID",account.getAccountId());

        request.getRequestDispatcher("AccountInfo.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
