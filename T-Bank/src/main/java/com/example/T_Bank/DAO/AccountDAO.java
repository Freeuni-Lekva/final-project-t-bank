package com.example.T_Bank.DAO;


import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.CardInfo;
import com.example.T_Bank.Storage.CardType;

import javax.smartcardio.Card;
import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {
    public Account login(String userName, String password);
    public Account register(String firstName, String lastName, String personalId,
                            String userName, String password, String birthdate);
}
