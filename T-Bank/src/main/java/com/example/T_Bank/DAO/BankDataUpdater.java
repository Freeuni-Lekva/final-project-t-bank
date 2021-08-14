package com.example.T_Bank.DAO;

import java.sql.*;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BankDataUpdater extends TimerTask {
    private Connection connection;
    public BankDataUpdater(Connection connection){
        this.connection = connection;
    }
    @Override
    public void run() {
        System.out.println(new Timestamp(System.currentTimeMillis()));
        updateLoans();
    }

    private void closeLoan(int accountLoanId, ResultSet rs){
        try {
            double fullMoney = rs.getDouble(7);
            double payedAmount = rs.getDouble(13);
            double toUpdateValue = fullMoney - payedAmount;
            String closeLoanQuery = "update account_loans " +
                    "set payed_amount = ?, " +
                    "to_pay_amount = 0, " +
                    "active_loan = ? " +
                    "where account_loan_id = ? ";
            PreparedStatement stm = connection.prepareStatement(closeLoanQuery);
            stm.setDouble(1, fullMoney);
            stm.setBoolean(2, false);
            stm.setInt(3, accountLoanId);
            stm.executeUpdate();
            String cardIdentifier = rs.getString(3);
            String cardCheck = "update account_cards " +
                    "set gel_balance = gel_balance - ? " +
                    "where card_identifier = ?";
            stm = connection.prepareStatement(cardCheck);
            stm.setDouble(1, toUpdateValue);
            stm.setString(2, cardIdentifier);
            stm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void updateLoan(int accountLoanId, long minuteDiff, ResultSet rs,
                            Timestamp startDate, Timestamp endDate, Timestamp nowDate){
        try {
            int periods = rs.getInt(6);
            if(minuteDiff >= periods){
                closeLoan(accountLoanId, rs);
                return;
            }
            double monthlyPayment = rs.getDouble(8);
            double payedAmount = rs.getDouble(13);
            double mustBe = monthlyPayment * minuteDiff;
            double toUpdateValue = mustBe - payedAmount;
            String updateLoanQuery = "update account_loans " +
                    "set payed_amount = payed_amount + ?, to_pay_amount = to_pay_amount - ?" +
                    " where account_loan_id = ?";
            PreparedStatement stm = connection.prepareStatement(updateLoanQuery);
            stm.setDouble(1, toUpdateValue);
            stm.setDouble(2, toUpdateValue);
            stm.setInt(3, accountLoanId);
            stm.executeUpdate();
//            System.out.println(stm);
            String cardIdentifier = rs.getString(3);
            String updateCard = "update account_cards " +
                    "set gel_balance = gel_balance - ? " +
                    "where card_identifier = ?";
            stm = connection.prepareStatement(updateCard);
            stm.setDouble(1, toUpdateValue);
            stm.setString(2, cardIdentifier);
            stm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public void updateLoans(){
        String allLoans = "select * from account_loans where active_loan = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(allLoans);
            stm.setBoolean(1, true);
            ResultSet resultSet = stm.executeQuery();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            while(resultSet.next()){
                int accountLoanId = resultSet.getInt(1);
                Timestamp startDate = resultSet.getTimestamp(9);
                Timestamp endDate = resultSet.getTimestamp(11);
                long minuteDiff = getDateDiff(startDate.getTime(), now.getTime(), TimeUnit.MINUTES);
                if(minuteDiff >= 1){
                    updateLoan(accountLoanId, minuteDiff, resultSet, startDate, endDate, now);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static long getDateDiff(long timeUpdate, long timeNow, TimeUnit timeUnit)
    {
        long diffInMillies = Math.abs(timeNow - timeUpdate);
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
