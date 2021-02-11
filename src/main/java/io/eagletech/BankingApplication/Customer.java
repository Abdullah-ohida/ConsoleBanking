package io.eagletech.BankingApplication;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    @Getter
    private String customerFirstName;
    @Getter
    private String customerLastName;
    private String customerAddress;
    private List<Account> myAccounts;
    @Getter @Setter
    private String bvn;
    public Customer(String customerFirstName, String customerLastName, String customerAddress) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerAddress = customerAddress;
        this.myAccounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        myAccounts.add(account);
    }
}
