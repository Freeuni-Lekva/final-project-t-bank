package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class ApplicationListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public ApplicationListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TBankDAO tBankDAO = new TBankDAO();
        Map<String, Account> sessions = new HashMap<>();
        tBankDAO.register("Giorgi", "Urumashvili", "12341234123", "giorgiurumashvili", "1234", "2000-16-04");

        sce.getServletContext().setAttribute("TBankDAO", tBankDAO);
        sce.getServletContext().setAttribute("Sessions", sessions);

        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
