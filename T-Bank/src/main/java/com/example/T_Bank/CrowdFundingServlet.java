package com.example.T_Bank;

import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CrowdFundingEvent;
import com.example.T_Bank.Storage.EventList;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CrowdFundingServlet", value = "/CrowdFundingServlet")
public class CrowdFundingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context=getServletContext();
        TBankDAO tBankDAO = (TBankDAO) context.getAttribute("TBankDAO");
        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        String personalID = account.getPersonalId();
        EventList wrapper=tBankDAO.getSpecificEvents(personalID);
        ArrayList<CrowdFundingEvent> eventList=wrapper.getAllEvents();
        ArrayList<CrowdFundingEvent> activeList=getActiveEvents(eventList);


        request.setAttribute("EventList",activeList);
        request.getRequestDispatcher("CrowdFundingPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context=getServletContext();
        TBankDAO tBankDAO = (TBankDAO) context.getAttribute("TBankDAO");
        Map<String, Account> sessions = (Map<String, Account>) context.getAttribute("Sessions");
        Account account = sessions.get(request.getSession().getId());
        String personalID = account.getPersonalId();
        EventList wrapper=tBankDAO.getSpecificEvents(personalID);
        ArrayList<CrowdFundingEvent> eventList=wrapper.getAllEvents();
        ArrayList<CrowdFundingEvent> activeList=getActiveEvents(eventList);


        CrowdFundingEvent event=getEventWithID(request,activeList);
        System.out.println(event.getEventId());
        System.out.println(event.getEventName());

        if(request.getParameter(activeList.indexOf(event)+"Modify")!=null) {
            int newTarget= Integer.parseInt(request.getParameter("targetAmount"));
            tBankDAO.changeEventTarget(event.getEventId(),newTarget);
            wrapper=tBankDAO.getSpecificEvents(personalID);
            eventList=wrapper.getAllEvents();
            activeList=getActiveEvents(eventList);
            request.setAttribute("EventList",activeList);
            request.getRequestDispatcher("CrowdFundingPage.jsp").forward(request, response);
        }

        if(request.getParameter(activeList.indexOf(event)+"Delete")!=null) {
            tBankDAO.deleteCrowdFundingEvent(event.getEventId());
            wrapper=tBankDAO.getSpecificEvents(personalID);
            eventList=wrapper.getAllEvents();
            activeList=getActiveEvents(eventList);
            request.setAttribute("EventList",activeList);
            request.getRequestDispatcher("CrowdFundingPage.jsp").forward(request, response);
        }

    }


    private CrowdFundingEvent getEventWithID(HttpServletRequest request, ArrayList<CrowdFundingEvent> events) {
        for(int i=0;i<events.size();i++) {
            if(request.getParameter((i+"Modify"))!=null) {
                return events.get(i);
            }
            if(request.getParameter(i+"Delete")!=null) {
                return events.get(i);
            }
        }
        return null;
    }


    private ArrayList<CrowdFundingEvent> getActiveEvents(ArrayList<CrowdFundingEvent> events) {
        ArrayList<CrowdFundingEvent> activeEvents=new ArrayList<>();
        for(int i=0;i<events.size();i++) {
            if(events.get(i).isActive()) {
                activeEvents.add(events.get(i));
            }
        }

        return activeEvents;
    }
}
