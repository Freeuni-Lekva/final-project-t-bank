import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class Tester {

    @Test
    public void testSimple(){
        TBankDAO dao = new TBankDAO();
        Account natenaAccount = dao.register("natena", "natenadze", "00700700700",
                    "nateasfafna007", "zaza-testlia", "");
        System.out.println(natenaAccount);

    }
}
