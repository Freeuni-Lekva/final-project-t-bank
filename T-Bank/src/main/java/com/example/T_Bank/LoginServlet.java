package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context=getServletContext();
        TBankDAO tBankDAO= (TBankDAO) context.getAttribute("TBankDAO");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Account account=tBankDAO.login(username,password);
        if(account.isValidAccount()) {
            context.setAttribute("Account", account);
            request.setAttribute("firstName",account.getFirstName());
            request.setAttribute("username",username);
            request.getRequestDispatcher("AccountPage.jsp").forward(request,response);
        } else {
            request.setAttribute("username",account.getErrorMessage());
            request.getRequestDispatcher("index.jsp").forward(request,response);
        }
        //else another jsp pops up.

    }
}
