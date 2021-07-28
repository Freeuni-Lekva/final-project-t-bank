package com.example.T_Bank.Storage;

public class Account {
    private String userName;
    private String firstName;
    private String lastName;
    private String personalId;
    private String password;
    private int accountId;

    public Account(String userName, String firstName, String lastName,
                   String personalId, String password, int accountId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.password = password;
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
