package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.AccountDAO;
import com.example.T_Bank.Storage.Account;
import com.example.T_Bank.Storage.ErrorMessage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Set;

public class AccountDAOImplementation implements AccountDAO {
    private Connection connection;
    ArrayList<Account> accounts;
    public AccountDAOImplementation(Connection connection){
        this.connection = connection;
        accounts = new ArrayList<>();
    }

    @Override
    public Account login(String userName, String password) {
        for(int i = 0; i < accounts.size(); i++){
            if(accounts.get(i).getUserName().equals(userName) &&
                            accounts.get(i).getPassword().equals(password)){
                return accounts.get(i);
            }
        }
        Account badAccount = new Account(null, null, null, null,
                null, -1, false, ErrorMessage.AccountNotValid);
        return badAccount;
    }

    @Override
    public Account register(String firstName, String lastName, String personalId,
                            String userName, String password, String birthdate) {
        for(int i = 0; i < accounts.size(); i++){
            Account next = accounts.get(i);
            if(next.getUserName().equals(userName)){
                Account badAccount = new Account(null, null, null, null,
                        null, -1, false, ErrorMessage.UserNameAlreadyExists);
                return badAccount;
            }
            if(next.getPersonalId().equals(personalId)) {
                Account badAccount = new Account(null, null, null, null, null,
                        -1, false, ErrorMessage.PersonalIdAlreadyExists);
                return badAccount;
            }
        }
        Account newAccount = new Account(userName, firstName, lastName,
                    personalId, password, accounts.size() + 1, true, ErrorMessage.NoErrorMessage);
        accounts.add(newAccount);
        return newAccount;
    }
}
