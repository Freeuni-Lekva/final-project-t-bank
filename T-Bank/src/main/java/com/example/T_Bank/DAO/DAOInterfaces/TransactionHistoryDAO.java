package com.example.T_Bank.DAO.DAOInterfaces;

import com.example.T_Bank.Storage.Transaction;

import java.util.ArrayList;
import java.util.Date;

public interface TransactionHistoryDAO {
    public ArrayList<Transaction> getAllTransactions(int accountId);
    public ArrayList<Transaction> getMoneyTransfers(int accountId);
    public ArrayList<Transaction> getAllIncomes(int accountId);
    public ArrayList<Transaction> getAllExpenses(int accountId);
    public ArrayList<Transaction> getAllConversions(int accountId);

    public Transaction logTransaction(int senderAccountId, int receiverAccountId, String senderCardIdentifier, String receiverCardIdentifier, int transactionTypeId, Date date, int currencyId,double amount);
 }
