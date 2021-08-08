package com.example.T_Bank.Storage;

public class Account {
    private String userName;
    private String firstName;
    private String lastName;
    private String personalId;
    private String password;
    private int accountId;
    private boolean isValidAccount;
    private ErrorMessage errorMessage;


    public Account(String userName, String firstName, String lastName,
                   String personalId, String password, int accountId,
                   boolean isValidAccount, ErrorMessage errorMessage) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.password = password;
        this.accountId = accountId;
        this.isValidAccount = isValidAccount;
        this.errorMessage = errorMessage;
    }

    public boolean isValidAccount() {
        return isValidAccount;
    }

    public void setValidAccount(boolean validAccount) {
        isValidAccount = validAccount;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getPassword() {
        return password;
    }

    public int getAccountId() {
        return accountId;
    }

}
