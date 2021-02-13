package io.eagletech.BankingApplication;

public class InvalidPinException extends BankingApplicationException{

    public InvalidPinException(String message) {
        super(message);
    }
}
