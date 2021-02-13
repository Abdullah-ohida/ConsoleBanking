package io.eagletech.BankingApplication;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Account implements Storable{
    @Getter
    private String acountName;
    private String accountNumber;
    private String customerBvn;
    private String bankName;
    private AccountType accountType;

    private int pin;

    @Getter
    private BigDecimal accountBalance;
    public Account(Customer customer, String accountNumber, String bankName, AccountType accountType) {
        this.acountName = customer.getCustomerFirstName()+ " "+ customer.getCustomerLastName();
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        customerBvn = customer.getBvn();
        accountBalance = BigDecimal.ZERO;
        this.bankName = bankName;


    }

    @Override
    public String toString(){
        String accountProfile ="";
        accountProfile+="Account Name: "+acountName+"\n";
        accountProfile+="Account Number: "+accountNumber+"\n";
        accountProfile+="Bank Name: "+bankName+"\n";
        accountProfile+="Account Type: "+accountType.toString()+"\n";
        accountProfile+="Customer BVN: "+customerBvn+"\n";
        //@todo remove account balance later, too sensitive to be in a toString method;
        accountProfile+="Account Balance: "+accountBalance+"\n";
        return accountProfile;

    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getId() {
        return accountNumber;
    }

    public void deposit(BigDecimal amountToDeposit) {
        accountBalance = accountBalance.add(amountToDeposit);
    }

    private void setPin(int newPin){
        pin = newPin;
    }
     void updatePin(int oldPin, int newPin) {
        if(oldPin== pin){
            setPin(newPin);
        }
    }

    public void withDraw(BigDecimal amountToWithdraw, int accountPin) {
        try{
            if(accountPin == getPin()){
                accountBalance = accountBalance.subtract(amountToWithdraw);
            }
            else{
                throw new WithdrawFailedException("Incorrect Pin");
            }


        }
        catch (InvalidPinException invalidPinException){
            throw new WithdrawFailedException(invalidPinException.getMessage());
        }
    }

    private int getPin() {
        if(pin == 0){
            throw new InvalidPinException("Pin not Set");
        }
        else{
            return pin;
        }
    }
}
