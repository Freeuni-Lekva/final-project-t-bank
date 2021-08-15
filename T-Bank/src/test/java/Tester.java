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
        tbank.register("koba", "paikidze", "00001111999", "koba",
                "kobasaba", null);
        for(int i = 1; i <= 10; i++) {
            LoanErrorMessage loan = tbank.takeLoan(1, "TBMTSC00001", i * 100, i + 1);
        }



    }

    public static void main(String[] args) {
        TBankDAO dao = new TBankDAO();
        dao.register("koba", "paikidze", "00001111999", "koba",
                "kobasaba", null);
        dao.openDeposit(1, "TBMTSC00001", 1, 100, "depo1");

    }

}
