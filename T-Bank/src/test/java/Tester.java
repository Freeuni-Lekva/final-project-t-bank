import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Tester {

    @Test
    public void testSimple(){
        TBankDAO dao = new TBankDAO();
        List <CardType> cards = dao.getCardTypes();
        for(int i = 0; i < cards.size(); i++){
            CardType card = cards.get(i);
            System.out.println(card.getCardTypeId() + " " + card.getCardTypeName() + " " +
                    card.getDescription() + " " + card.getMaxLimit());
        }
    }
}
