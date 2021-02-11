package io.eagletech.BankingApplication;

import lombok.Getter;

import java.math.BigDecimal;

public class Account {
    private String acountName;
    private String accountNumber;
    private String customerFirstName;
    private String customerLastName;
    @Getter
    private BigDecimal accountBalance;
    public Account(Customer customer, String accountNumber) {
        this.acountName = customer.getCustomerFirstName()+ " "+ customer.getCustomerLastName();
        this.accountNumber = accountNumber;
        this.customerFirstName = customer.getCustomerFirstName();
        this.customerLastName = customer.getCustomerLastName();
        accountBalance = BigDecimal.ZERO;


    }
}
