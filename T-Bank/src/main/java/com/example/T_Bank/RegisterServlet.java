package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private String firstName;
    private String lastName;
    private String id;
    private String birthDate;
    private String username;
    private String password;
    private String repeat;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/RegisterPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        TBankDAO tBank = (TBankDAO) context.getAttribute("TBankDAO");

        getCredentials(request);
        String errorMessage = getErrorMessage(tBank, request, response);

        update(response, errorMessage);
    }

    private String getErrorMessage(TBankDAO tBank, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String res = "";
        if (password.length() < 8) {
            res = "Password must be at least 8 characters long!";
        } else if (!password.equals(repeat)) {
            res = "Passwords don't match!";
        } else if (fileIsEmpty()) {
            res = "Please fill in all the fields";
        } else {
            Account account = tBank.register(firstName, lastName, id, username, password, birthDate);
            if (account.isValidAccount()) {
                tBank.login(username, password);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                res = account.getErrorMessage().toString();
            }
        }
        return res;
    }

    private void update(HttpServletResponse response, String errorMessage) throws IOException {
        PrintWriter hp = response.getWriter();
        String docType = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
                "Transitional//EN\">\n";
        hp.print(docType +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title>Register</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<div>\n" +
                "    <form action=\"RegisterServlet\" method=\"post\" class=\"registrationForm\">\n" +
                "        <H1 name=\"title\">Create New Account</H1>\n" +
                "        <br>\n" +
                "        <label>First name</label><br>\n" +
                "        <input type=\"text\" name=\"firstName\" placeholder=\"Enter your First Name...\"><br><br>\n" +
                "        <label>Last name</label><br>\n" +
                "        <input type=\"text\" name=\"lastName\" placeholder=\"Enter your Last Name...\"><br><br>\n" +
                "        <label>ID</label><br>\n" +
                "        <input type=\"text\" name=\"id\" placeholder=\"Enter your ID...\"><br><br>\n" +
                "        <label>Birth Date</label><br>\n" +
                "        <input type=\"text\" name=\"birthDate\" placeholder=\"Enter your Birth Date...\"><br><br>\n" +
                "        <label>User Name</label><br>\n" +
                "        <input type=\"text\" name=\"username\" placeholder=\"Enter your User Name...\"><br><br>\n" +
                "        <label>Password</label><br>\n" +
                "        <input type=\"password\" name=\"password\" placeholder=\"Enter your Password...\"><br><br>\n" +
                "        <label>Confirm Password</label><br>\n" +
                "        <input type=\"password\" name=\"repeatPassword\" placeholder=\"Confirm your Password...\"><br>\n" +
                "        <h3 style=\"color: red\">" + errorMessage + "</h3>\n" +
                "        <button type=\"submit\">Create Account</button>\n" +
                "        <h6>Already an user? <a href=\"index.jsp\">Log in</a></h6>\n" +
                "    </form>\n" +
                "</div>\n" +
                "</body>\n" +
                "\n" +
                "</html>");
    }

    private void getCredentials(HttpServletRequest request) {
        firstName = request.getParameter("firstName");
        lastName = request.getParameter("lastName");
        id = request.getParameter("id");
        birthDate = request.getParameter("birthDate");
        username = request.getParameter("username");
        password = request.getParameter("password");
        repeat = request.getParameter("repeatPassword");
    }

    private boolean fileIsEmpty() {
        if (firstName.length() == 0) {
            return true;
        }
        if (lastName.length() == 0) {
            return true;
        }
        if (username.length() == 0) {
            return true;
        }
        if (id.length() == 0) {
            return true;
        }
        if (birthDate.length() == 0) {
            return true;
        }

        return false;
    }
}
