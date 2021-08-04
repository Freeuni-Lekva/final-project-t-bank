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
//        String test = "test";
//        List<CardType> cardTypes = dao.getCardTypes();
//        for(int i = 0; i < 10; i++){
//            String tmp = test + i;
//            dao.register(tmp, tmp, tmp, tmp, tmp, tmp);
//            dao.addCard(i + 1, cardTypes.get(i % 2 + 1), tmp + i);
//        }
//        AccountNumbersList list = dao.getAccountNumbers("test120");
//        System.out.println(list.getErrorMessage());
        List<Currency> currencies = dao.getCurrencies();
        TransferError error = dao.transferMoney("TBMTSC00001",
                "TBMTSC00003", 100,
                currencies.get(0), currencies.get(2));
        System.out.println(error);

    }

}
