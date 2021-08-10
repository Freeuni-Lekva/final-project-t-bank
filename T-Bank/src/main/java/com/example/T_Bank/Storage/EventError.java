package com.example.T_Bank.Storage;

public enum EventError {
//    unique(event_name, account_id, active)
    sameEventNameOnThisAccount, noErrorMessage, noEventFound, targetMoneyLessThanZero, noAccountFound, notEnoughAmount, noCardFound, eventNotActive
}
