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

        ArrayList<Transaction> list=tbank.getAllConversions(1);
        System.out.println(list.get(0).toString());
        System.out.println(list.get(1).toString());
        System.out.println(list.get(2).toString());
        System.out.println(list.get(3).toString());
        System.out.println(list.get(4).toString());
        System.out.println(list.get(5).toString());
        System.out.println(list.get(6).toString());
        System.out.println(list.get(7).toString());
        System.out.println(list.get(8).toString());

    }

}
