package io.eagletech.bankingApplication.exceptions;

import io.eagletech.bankingApplication.exceptions.BankingApplicationException;

public class DepositFailedException extends BankingApplicationException {
    public DepositFailedException(String message){
        super(message);
    }
}
