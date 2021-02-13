package io.eagletech.BankingApplication;

import lombok.Getter;

import java.util.List;

public  class Bank {

    private final String bankFullName;
    private final String bankShortName;
    @Getter
    private  final String bankCode;
    private Storable<Account> accountDatabase;
    public Bank(String bankFullName, String bankShortName, String bankUniqueSixDigitCode) {
        this.bankFullName = bankFullName;
        this.bankShortName = bankShortName;
        bankCode = bankUniqueSixDigitCode;
        accountDatabase = new Database<>();
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
        Account account = new Account(customer,accountNumber, this.bankFullName);
        customer.addAccount(account);
        addAccount(account);
    }

    private String generateAccountNumberForCustomer() {
        String accountNumber= bankCode+ String.format("%03d", accountDatabase.size()+1);
        accountNumber = accountNumber+ generateCheckDigit(bankCode, accountNumber);
        
        return accountNumber;
    }

    private String generateCheckDigit(String bankCode, String accountNumber) {
        String numberToVerify = bankCode+accountNumber;
//        A*3+B*7+C*3+D*3+E*7+F*3+G*3+H*7+I*3+J*3+K*7+L*3+M*3+N*7+O*3\
        int[] checkDigitMultiplier = {3,7,3,3,7,3,3,7,3,3,7,3,3,7,3};
        int result= 0;

                for(int i = 0; i<checkDigitMultiplier.length; i++){
                    result += checkDigitMultiplier[i]* numberToVerify.charAt(i);
                }

                int modulo = result%10;
                int subtract = Math.abs(modulo - 10);
                if (subtract == 10){
                    return "0";
                }
        return String.format("%d", subtract);
    }

    private void addAccount(Account account) {
        accountDatabase.save(account);
    }

    public List<Account> getRegisteredCustomers() {
        return accountDatabase.findAll();
    }
}
