package io.eagletech.BankingApplication;

public enum TransactionType {
    DEBIT, CREDIT, TRANSFER_IN, TRANSFER_OUT;

    public String toString(){
        return switch(this){
            case DEBIT-> "Debit Transaction";
            case CREDIT -> "Credit Transaction";
            case TRANSFER_IN -> "Transfer In";
            case TRANSFER_OUT -> "Funds Transfer out";
        };
    }
}
