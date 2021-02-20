package io.eagletech.bankingApplication.models;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Transaction {
    private final String transactionId;
    private final TransactionType transactionType;
    private final LocalDateTime transactionDate;
    private final BigDecimal transactionAmount;
    private final String actorName;

    public Transaction (String transactionId, TransactionType transactionType, BigDecimal transactionAmount, String actorName){
            this.transactionId = transactionId;
            this.transactionType = transactionType;
            this.transactionAmount = transactionAmount;
            this.actorName = actorName;
            transactionDate = LocalDateTime.now();

    }
}
