package io.eagletech.BankingApplication;

import lombok.Getter;

import java.math.BigDecimal;

public class Account {
    private String acountName;
    private String accountNumber;
    private String customerFirstName;
    private String customerLastName;
    private String bankName;
    @Getter
    private BigDecimal accountBalance;
    public Account(Customer customer, String accountNumber, String bankName) {
        this.acountName = customer.getCustomerFirstName()+ " "+ customer.getCustomerLastName();
        this.accountNumber = accountNumber;
        this.customerFirstName = customer.getCustomerFirstName();
        this.customerLastName = customer.getCustomerLastName();
        accountBalance = BigDecimal.ZERO;
        this.bankName = bankName;


    }

    @Override
    public String toString(){
        String accountProfile ="";
        accountProfile+="Account Name: "+acountName+"\n";
        accountProfile+="Account Number: "+accountNumber+"\n";
        accountProfile+="Bank Name: "+bankName+"\n";
        //@todo remove account balance later, too sensitive to be in a toString method;
        accountProfile+="Account Balance: "+accountBalance+"\n";
        return accountProfile;

    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
