import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Tester {

    @Test
    public void testSimple() {
        TBankDAO dao = new TBankDAO();
        String test = "test";
        List<CardType> cardTypes = dao.getCardTypes();
        for(int i = 0; i < 10; i++){
            String tmp = test + i;
            dao.register(tmp, tmp, tmp, tmp, tmp, tmp);
            dao.addCard(i + 1, cardTypes.get(i % 2 + 1), tmp + i);
        }


    }

}
