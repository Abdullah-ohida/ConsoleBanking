package io.eagletech.bankingApplication.notification;

import io.eagletech.bankingApplication.Account;
import io.eagletech.bankingApplication.models.Transaction;

public class SmsAlert extends Alert {
    private String alertTitle;
    private String amount;
    private String amountInWords;
    private String accountNumber;
    private  String transactionType;
    private String actorName;
    private String availableBalance;
    private String transactionId;
    private String transactionDate;

    public SmsAlert(Account chibuzoAccount, Transaction transaction) {
        super();
        transactionType = transaction.getTransactionType().toString();
        alertTitle = transactionType + " Notification";
        amount = transaction.getTransactionAmount().toString();
        accountNumber = chibuzoAccount.getAccountNumber();
        actorName = transaction.getActorName();
        transactionDate = transaction.getTransactionDate().toString();
        availableBalance = chibuzoAccount.calculateAccountBalance().toString();
        transactionId = transaction.getTransactionId();
        amountInWords = amount;





    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(alertTitle.toUpperCase()).append("\n");
        stringBuilder.append(transactionId).append("\n");
        stringBuilder.append(transactionDate).append("\n");
        stringBuilder.append(accountNumber).append("\n");
        stringBuilder.append(amount).append("\n");
        stringBuilder.append(actorName).append("\n");
        stringBuilder.append(transactionType).append("\n");
        stringBuilder.append(availableBalance).append("\n");

        return stringBuilder.toString();

    }
}
