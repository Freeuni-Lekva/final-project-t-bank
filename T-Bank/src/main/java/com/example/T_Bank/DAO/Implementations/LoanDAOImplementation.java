package com.example.T_Bank.DAO.Implementations;

import com.example.T_Bank.DAO.DAOInterfaces.LoanDAO;
import com.example.T_Bank.Storage.ErrorMessage;
import com.example.T_Bank.Storage.Loan;
import com.example.T_Bank.Storage.LoanErrorMessage;
import com.example.T_Bank.Storage.LoanList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class LoanDAOImplementation implements LoanDAO {
    private Connection connection;
    private static final double PERCENT = 0.15;
    private static final long MINUTES_PERIOD = 1 * 1000 * 60;
    public LoanDAOImplementation(Connection connection) {
        this.connection = connection;
    }

    private boolean checkColumnValidity(String columnName, String table, String strValue, int intValue){
        String checkQuery = "select count(*) from " + table + " where " + columnName + " = ?";
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(checkQuery);
            if(strValue == null){
                stm.setInt(1, intValue);
            }else{
                stm.setString(1, strValue);
            }
            ResultSet rs = stm.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if(count == 0){
                return false;
            }
            if(count == 1){
                return true;
            }
            if(count > 1){
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Loan badLoan(LoanErrorMessage errorMessage){
        return new Loan(0, 0, null, 0,
                0, 0, 0, 0, null, null, null,
                false, false, errorMessage, 0, 0);

    }

    @Override
    public LoanErrorMessage takeLoan(int accountId, String cardIdentifier, double startMoney, int periods) {
        if(!checkColumnValidity("account_id", "accounts", null, accountId)){
            return LoanErrorMessage.accountIdNotValid;
        }
        if(!checkColumnValidity("card_identifier", "account_cards", cardIdentifier, 0)){
            return LoanErrorMessage.cardNotValid;
        }
        if(startMoney < 0){
            return LoanErrorMessage.requesterMoneyLessThanZero;
        }
        if(periods < 1){
            return LoanErrorMessage.periodsLessThanZero;
        }
        Random random = new Random();
        int nextInt = random.nextInt(100);
        nextInt = nextInt % 100;
        if(nextInt < 0){
            nextInt = -nextInt;
        }
        if(nextInt > 90){
            return LoanErrorMessage.bankDidNotAcceptTheLoan;
        }
        return addLoan(accountId, cardIdentifier, startMoney, periods);
    }


    private double calculateFinalMoney(double startMoney, double yearCount){
        return startMoney * Math.pow((1 + PERCENT), yearCount);
    }

    private LoanErrorMessage addLoan(int accountId, String cardIdentifier, double startMoney, int periods){
        String addQuery = "insert into account_loans(account_id, card_identifier, start_money, percent, " +
                "periods, full_money, monthly_payment, start_date, last_update_date, end_date, active_loan, " +
                "payed_amount, to_pay_amount) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {

            double yearCount = (double)periods / 12.0;

            double fullMoney = calculateFinalMoney(startMoney, yearCount);
            double monthlyPayment = fullMoney / periods;

            Timestamp startTime = new Timestamp(System.currentTimeMillis());
            long current = startTime.getTime();
            Timestamp endTime = new Timestamp(current + periods * MINUTES_PERIOD);


            PreparedStatement stm = connection.prepareStatement(addQuery);
            stm.setInt(1, accountId);
            stm.setString(2, cardIdentifier);
            stm.setDouble(3, startMoney);
            stm.setDouble(4, PERCENT);
            stm.setInt(5, periods);
            stm.setDouble(6, fullMoney);
            stm.setDouble(7, monthlyPayment);
            stm.setTimestamp(8, startTime);
            stm.setTimestamp(9, startTime);
            stm.setTimestamp(10, endTime);
            stm.setBoolean(11, true);
            stm.setDouble(12, 0);
            stm.setDouble(13, fullMoney);
            stm.executeUpdate();

            String addMoney = "update account_cards " +
                    "set gel_balance = gel_balance + ?" +
                    " where card_identifier = ?";
            stm = connection.prepareStatement(addMoney);
            stm.setDouble(1, startMoney);
            stm.setString(2, cardIdentifier);
            stm.executeUpdate();

            return LoanErrorMessage.noErrorMessage;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return LoanErrorMessage.accountIdNotValid;
    }

    private ArrayList<Loan> getLoans(ResultSet rs){
        ArrayList <Loan> loans = new ArrayList<>();
        while(true){
            try {
                if (!rs.next()) break;
                loans.add(new Loan(rs.getInt(1), rs.getInt(2),  rs.getString(3),
                        rs.getDouble(4), rs.getDouble(5), rs.getInt(6),
                        rs.getDouble(7), rs.getDouble(8), rs.getDate(9),
                        rs.getDate(10),rs.getDate(11),  rs.getBoolean(12),
                        true, LoanErrorMessage.noErrorMessage, rs.getDouble(13), rs.getDouble(14)));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return loans;
    }

    @Override
    public LoanList getAllLoans(int accountId) {
        if(!checkColumnValidity("account_id", "accounts", null, accountId)){
            return new LoanList(null, false, LoanErrorMessage.accountIdNotValid);
        }
        String allLoans = "select * from account_loans where account_id = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(allLoans);
            stm.setInt(1, accountId);
            ResultSet rs = stm.executeQuery();
            ArrayList<Loan> loans = getLoans(rs);
            LoanList loanList = new LoanList(loans, true, LoanErrorMessage.noErrorMessage);
            return loanList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new LoanList(null, false, LoanErrorMessage.accountIdNotValid);

    }



}
