import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.Account;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class Tester {

    @Test
    public void testSimple(){
        TBankDAO dao = new TBankDAO();
//        Account takoAccount = dao.register("tako", "tristanichi", "00000000001",
//                "tjz", "cimaka", null);
//        System.out.println(takoAccount);
        Account bandziTako = dao.register("tako", "tristanichi", "00000000001",
                "tjz", "cimaka", null);
        System.out.println(bandziTako);
        Account ufroBandziTako = dao.register("tako", "tristanichi", "00000000001",
                "tjz-001", "cimaka", null);
        System.out.println(ufroBandziTako);

    }
}
