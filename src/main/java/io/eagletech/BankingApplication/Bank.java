package io.eagletech.BankingApplication;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public  class Bank implements Storable{

    private final String bankFullName;
    private final String bankShortName;
    @Getter
    private  final String bankCode;
    private Database<Account> accountDatabase;
    public Bank(String bankFullName, String bankShortName, String bankUniqueSixDigitCode) {
        this.bankFullName = bankFullName;
        this.bankShortName = bankShortName;
        bankCode = bankUniqueSixDigitCode;
        accountDatabase = new DatabaseImpl<>();
    }

    public void register(Customer customer, AccountType accountType) {
        if(customer.getBvn() == null){
            CentralBank.createCentralBank().registerCreateBvnFor(customer);
        }
        String accountNumber = generateAccountNumberForCustomer();
        Account account = new Account(customer,accountNumber, this.bankFullName, accountType);
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


    @Override
    public String toString(){
        StringBuilder bankToString = new StringBuilder();
        bankToString.append("Bank Name: ").append(bankFullName).append("\n");
        bankToString.append("Bank Short Name: ").append(bankShortName).append("\n");
        bankToString.append("Bank Code: ").append(bankCode).append("\n");

        return bankToString.toString();
    }

    public void closeAccountFor(Customer customer,String accountNumber) {
        Optional<Account> account = accountDatabase.findById(accountNumber);
        account.ifPresent(account1 -> {
                    if (!account.get().getAcountName().equalsIgnoreCase(customer.getCustomerFirstName() + " " + customer.getCustomerLastName())) {
                        throw new BankingApplicationException("Account number does not belong to customer");
                    }
                    accountDatabase.delete(account1);
                }
        );
        customer.getMyAccount().remove(account);

    }

    @Override
    public String getId() {
        return bankCode;
    }

    public void depositMoneyIntoAccount(BigDecimal amountToDeposit, String customerAccountNumber) {
        Optional<Account> optionalAccount =accountDatabase.findById(customerAccountNumber);
        if(optionalAccount.isPresent()){
            optionalAccount.get().deposit(amountToDeposit);
        }
        else{
            throw new DepositFailedException("Account not found");
        }
    }

    public void withDrawMoneyFrom(String customerAccountNumber, BigDecimal amountToWithdraw, int accountPin) {
        Optional<Account> optionalAccount =accountDatabase.findById(customerAccountNumber);
        if(optionalAccount.isPresent()){
            optionalAccount.get().withDraw(amountToWithdraw, accountPin);
        }
        else{
            throw new WithdrawFailedException("Account not found");
        }
    }

    public void transfer(TransferRequest transferRequest) throws BankingApplicationException{
        Optional<Account> senderAccount = accountDatabase.findById(transferRequest.getSenderAccountNumber());
        Optional<Account> recieverAccount = accountDatabase.findById(transferRequest.getRecieverAccountNumber());
        if(senderAccount.isPresent() && recieverAccount.isPresent()){
            withDrawMoneyFrom(transferRequest.getSenderAccountNumber(), transferRequest.getAmountToTransfer(), transferRequest.getSenderAccountPin());
            depositMoneyIntoAccount(transferRequest.getAmountToTransfer(), transferRequest.getRecieverAccountNumber());
        }
    }
}
