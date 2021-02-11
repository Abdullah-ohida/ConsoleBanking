package io.eagletech.BankingApplication;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public  class Bank {

    private final String bankFullName;
    private final String bankShortName;
    @Getter
    private  final String bankCode;
    private List<Account> accountList;
    public Bank(String bankFullName, String bankShortName, String bankUniqueSixDigitCode) {
        this.bankFullName = bankFullName;
        this.bankShortName = bankShortName;
        bankCode = bankUniqueSixDigitCode;
        accountList = new ArrayList<>();
    }

    @Override
    public String toString(){
        StringBuilder bankToString = new StringBuilder();
        bankToString.append("Bank Name: ").append(bankFullName).append("\n");
        bankToString.append("Bank Short Name: ").append(bankShortName).append("\n");
        bankToString.append("Bank Code: ").append(bankCode).append("\n");

        return bankToString.toString();
    }


    public void register(Customer customer) {
        if(customer.getBvn() == null){
            CentralBank.createCentralBank().registerCreateBvnFor(customer);
        }
        String accountNumber = generateAccountNumberForCustomer();
        Account account = new Account(customer,accountNumber);
        customer.addAccount(account);
        addAccount(account);
    }

    private String generateAccountNumberForCustomer() {
        return "nothing";
    }

    private void addAccount(Account account) {
        accountList.add(account);
    }
}
