import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Tester {

    @Test
    public void testSimple(){
        TBankDAO dao = new TBankDAO();
        dao.register("koba", "p", "011", "kobakoba", "kobakoba",
                null);
        List<CardType>cardTypes = dao.getCardTypes();
        dao.addCard(1, cardTypes.get(0), "rame");
    }
}
