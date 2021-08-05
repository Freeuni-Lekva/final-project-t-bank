import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Tester {

    @Test
    public void testSimple() {
        TBankDAO dao = new TBankDAO();

/*
        dao.register("tako", "Jincharadze", "10010010022", "tjzze", "password", null);
*/


/*
        dao.register("koba", "cudipaikidze", "11111111111", "kobaa", "pass", null);
*/

        ArrayList<Currency> curr = dao.getCurrencies();
        /*curr.stream().forEach(s -> System.out.println(s.getCurrencyName()));*/



        /*System.out.println(dao.getExchangeValue(100, curr.get(0), curr.get(1)));
        System.out.println(dao.getExchangeValue(200, curr.get(1), curr.get(2)));
        System.out.println(dao.getExchangeValue(300, curr.get(1), curr.get(0)));
        System.out.println(dao.getExchangeValue(500, curr.get(0), curr.get(2)));*/

        /*dao.currencyExchange("TBMTSC00001", 100, curr.get(1), curr.get(0));*/
        System.out.println(dao.currencyExchange("TBMTSC00002", 308, curr.get(2), curr.get(0)));

    }

}
