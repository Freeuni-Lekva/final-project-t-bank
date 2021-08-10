import com.example.T_Bank.DAO.DAOInterfaces.CrowdFundingEventDAO;
import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Tester {

    @Test
    public void testSimple() {
        TBankDAO tbank = new TBankDAO();

        /*tbank.register("tako", "jincharadze" , "01111111111", "na", "tako", null);


        tbank.createCrowdFundingEvent("tako", 1, tbank.getAccountCards(1).get(0).getCardIdentifier(),
                "a", 300, tbank.getCurrencies().get(1));*/

       /* tbank.createCrowdFundingEvent("koba", 1, tbank.getAccountCards(1).get(0).getCardIdentifier(),
                "a", 500, tbank.getCurrencies().get(1));*/

     /*  tbank.changeEventTarget(2,200);*/

/*
       ArrayList<CrowdFundingEvent> event = tbank.getPublicCrowdFundingEvents();
       for (int i = 0; i < event.size(); i++) {
           System.out.println(event.get(i).getEventName());
       }*/

        Currency curr = tbank.getCurrencies().get(0);
        System.out.println(tbank.sendFunds(3, "TBMTSC00001", 4500, curr));

    }

}
