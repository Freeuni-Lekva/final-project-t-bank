package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.Deposit;

import java.sql.SQLException;
import java.util.ArrayList;


public interface DepositDAO {
    void closeDeposit(int depositID);
    void openDeposit(int accountID, String cardIdentifier, int periods, double amount, String depositName);
    void addAmountToDeposit(int depositID, double amount) throws SQLException;
    ArrayList<Deposit> getAllDeposits(int accountId);
}
