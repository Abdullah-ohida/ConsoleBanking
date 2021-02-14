package io.eagletech.BankingApplication;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferRequest {
    private final BigDecimal amountToTransfer;
    private final String senderAccountNumber;
    private final String receiverAccountNumber;
    private final int senderAccountPin;
    public TransferRequest(BigDecimal amountToTransfer, String senderAccountNumber, String receiverAccount, int senderAccountPin) {
        this.amountToTransfer = amountToTransfer;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccount;
        this.senderAccountPin = senderAccountPin;
    }
}
