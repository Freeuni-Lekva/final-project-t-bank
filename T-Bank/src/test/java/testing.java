import com.example.T_Bank.DAO.TBankDAO;
import com.example.T_Bank.Storage.LoanErrorMessage;
import org.junit.jupiter.api.Test;


public class testing {
    @Test
    public void test1() {
        TBankDAO tbank = new TBankDAO();
        tbank.register("koba", "paikidze", "00001111999", "koba",
                "kobasaba", null);

        tbank.addCard(1, tbank.getCardTypes().get(0), "tako");
    }
}
