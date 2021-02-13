package io.eagletech.BankingApplication;

public class WithdrawFailedException extends BankingApplicationException {
    public WithdrawFailedException(String message) {
        super(message);
    }
}
