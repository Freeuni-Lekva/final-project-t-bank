package com.example.T_Bank.DAO;


import com.example.T_Bank.Storage.Account;

import java.sql.SQLException;

public interface AccountDAO {
    public Account login(String userName, String password);
    public Account register(String firstName, String lastName, String personalId,
                            String userName, String password, String birthdate);
}
