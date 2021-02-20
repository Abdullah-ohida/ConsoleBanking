package io.eagletech.bankingApplication.exceptions;

import io.eagletech.bankingApplication.exceptions.BankingApplicationException;

public class InvalidPinException extends BankingApplicationException {

    public InvalidPinException(String message) {
        super(message);
    }
}
