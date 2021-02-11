package io.eagletech.BankingApplication;

import lombok.Getter;

public  class Bank {

    private final String bankFullName;
    private final String bankShortName;
    private  String bankCode;
    public Bank(String bankFullName, String bankShortName, String bankUniqueSixDigitCode) {
        this.bankFullName = bankFullName;
        this.bankShortName = bankShortName;
        bankCode = bankUniqueSixDigitCode;
    }

    @Override
    public String toString(){
        StringBuilder bankToString = new StringBuilder();
        bankToString.append("Bank Name: ").append(bankFullName).append("\n");
        bankToString.append("Bank Short Name: ").append(bankShortName).append("\n");
        bankToString.append("Bank Code: ").append(bankCode).append("\n");

        return bankToString.toString();
    }

    public String getBankCode() {
        return bankCode;
    }
}
