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

        updateDeposits();
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

    public void updateDeposits() {
        String allDeposits = "select * from account_deposits where active = ?";
        try {
            PreparedStatement depositStm = connection.prepareStatement(allDeposits);
            depositStm.setBoolean(1, true);
            ResultSet deposits = depositStm.executeQuery();
            Timestamp now = new Timestamp(System.currentTimeMillis());
//            System.out.println("ciklamde");
//            System.out.println(depositStm);
            while (deposits.next()) {
//                System.out.println("cikli");
                int accountDepositId = deposits.getInt(1);
                Timestamp startDate = deposits.getTimestamp(9);
                Timestamp endDate = deposits.getTimestamp(11);
//                System.out.println("startDate: " + startDate);
//                System.out.println("now: " + now);
                long minuteDiff = getDateDiff(startDate.getTime(), now.getTime(), TimeUnit.MINUTES);
                System.out.println("diff minute:" + minuteDiff);
                if (minuteDiff >= 1) {
//                    System.out.println("update unda shevide");
                    updateDeposit(accountDepositId, minuteDiff, deposits, startDate, endDate, now);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateDeposit(int accountDepositId, long minuteDiff, ResultSet rs,
                              Timestamp startDate, Timestamp endDate, Timestamp nowDate) {
//        System.out.println("update deposit");
        try {
            int periods = rs.getInt(8);
            if(minuteDiff >= periods){
                minuteDiff = periods;
            }
            double percent = rs.getDouble(7) / 100;
            double startMoney = rs.getDouble(13);
            double yearDiff = (double)minuteDiff / 12.0;

            double mustBe = startMoney * Math.pow(1 + percent, yearDiff);
//            double amount = rs.getDouble(5);
//            double toUpdate = mustBe - amount;

            String updateDepositQuery = "update account_deposits " +
                    "set balance = ?" +
                    " where deposit_id = ?";
            PreparedStatement stm = connection.prepareStatement(updateDepositQuery);
            stm.setDouble(1, mustBe);
            stm.setInt(2, accountDepositId);
            stm.executeUpdate();
//            System.out.println("deposit update query");
            System.out.println(stm);

            if (minuteDiff >= periods) {
                closeDeposit(accountDepositId, rs.getString(4), mustBe);
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateCard(String card, int currencyId,  double amount) {
        String updateQuery = "update account_cards " +
                "set gel_balance = gel_balance + ? " +
                "where card_identifier = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(updateQuery);
            stm.setDouble(1, amount);
            stm.setString(2, card);
//            System.out.println(stm);
            stm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void closeDeposit(int accountDepositId,
                              String cardIdentifier, double amount) {
        try {
            String closeDepositQuery = "update account_deposits " +
                    "set active = false" +
                    " where deposit_id = ? and active = true";
            PreparedStatement stm = connection.prepareStatement(closeDepositQuery);
            stm.setInt(1, accountDepositId);
            if(stm.executeUpdate() == 0) {
                return;
            }
            updateCard(cardIdentifier, 1, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
