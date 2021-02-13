package io.eagletech.BankingApplication;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferRequest {
    private final BigDecimal amountToTransfer;
    private final String senderAccountNumber;
    private final String recieverAccountNumber;
    private final int senderAccountPin;
    public TransferRequest(BigDecimal amountToTransfer, String senderAccountNumber, String recieverAccount, int senderAccountPin) {
        this.amountToTransfer = amountToTransfer;
        this.senderAccountNumber = senderAccountNumber;
        this.recieverAccountNumber = recieverAccount;
        this.senderAccountPin = senderAccountPin;
    }
}
