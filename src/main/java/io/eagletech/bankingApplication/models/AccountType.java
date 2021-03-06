package io.eagletech.bankingApplication.models;

public enum AccountType {
    SAVINGS, CURRENT, KIDDIES;

    @Override
    public String toString(){
        return switch (this){
            case CURRENT -> "Current Account";
            case KIDDIES -> "Kiddies Account";
            case SAVINGS -> "Savings Account";
        };
    }
}
