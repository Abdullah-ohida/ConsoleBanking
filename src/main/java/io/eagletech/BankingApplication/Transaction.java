package io.eagletech.BankingApplication;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    @Getter
    private final TransactionType transactionType;
    private final LocalDateTime transactionDate;
    @Getter
    private final BigDecimal transactionAmount;
    private String actorName;

    public Transaction (String transactionId, TransactionType transactionType, BigDecimal transactionAmount, String actorName){
            this.transactionId = transactionId;
            this.transactionType = transactionType;
            this.transactionAmount = transactionAmount;
            this.actorName = actorName;
            transactionDate = LocalDateTime.now();

    }
}
