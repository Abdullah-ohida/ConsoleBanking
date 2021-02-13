package io.eagletech.BankingApplication;

public class DepositFailedException extends BankingApplicationException {
    public DepositFailedException(String message){
        super(message);
    }
}
