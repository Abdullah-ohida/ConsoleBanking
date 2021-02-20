package io.eagletech.bankingApplication.exceptions;

import io.eagletech.bankingApplication.exceptions.BankingApplicationException;

public class WithdrawFailedException extends BankingApplicationException {
    public WithdrawFailedException(String message) {
        super(message);
    }
}
