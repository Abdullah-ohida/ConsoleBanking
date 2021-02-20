package io.eagletech.bankingApplication.models;

import io.eagletech.bankingApplication.Account;
import io.eagletech.bankingApplication.database.Database;
import io.eagletech.bankingApplication.database.DatabaseImpl;
import io.eagletech.bankingApplication.database.Storable;
import io.eagletech.bankingApplication.dtos.requestModels.TransferRequest;
import io.eagletech.bankingApplication.exceptions.BankingApplicationException;
import io.eagletech.bankingApplication.exceptions.DepositFailedException;
import io.eagletech.bankingApplication.exceptions.WithdrawFailedException;
import io.eagletech.bankingApplication.notification.NotificationService;
import io.eagletech.bankingApplication.notification.SmsNotification;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.eagletech.bankingApplication.models.TransactionType.*;

public  class Bank implements Storable {

    private final String bankFullName;

    private final String bankShortName;
    @Getter
    private  final String bankCode;

    private final Database<Account> accountDatabase;
    private final NotificationService notificationService = new SmsNotification();


    @Override
    public String getId() {
        return bankCode;
    }

    @Override
    public String getName() {
        return bankShortName;
    }



    public Bank(String bankFullName, String bankShortName, String bankUniqueSixDigitCode) {
        this.bankFullName = bankFullName;
        this.bankShortName = bankShortName.toLowerCase();
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



    public void closeAccountFor(Customer customer,String accountNumber) {
        Optional<Account> account = accountDatabase.findById(accountNumber);
        account.ifPresent(account1 -> {
                    if (!account.get().getAccountName().equalsIgnoreCase(customer.getCustomerFirstName() + " " + customer.getCustomerLastName())) {
                        throw new BankingApplicationException("Account number does not belong to customer");
                    }
                    accountDatabase.delete(account1);
            customer.getMyAccount().remove(account.get());
                }
        );


    }


    public void depositMoneyIntoAccount(BigDecimal amountToDeposit, String customerAccountNumber, TransactionType transactionType) {
        Optional<Account> optionalAccount =accountDatabase.findById(customerAccountNumber);
        if(optionalAccount.isPresent()){
          notifyCustomerViaSms(saveTransaction(optionalAccount.get(), transactionType, amountToDeposit), optionalAccount.get());
        }
        else{
            throw new DepositFailedException("Account not found");
        }

    }
    public void notifyCustomerViaSms(Transaction transaction, Account customerAccount ){
        notificationService.notify(notificationService.createAlert(customerAccount, transaction));
    }

    private Transaction saveTransaction(Account optionalAccount, TransactionType transactionType, BigDecimal transactionAmount) {
        Transaction transaction = new Transaction(generateTransactionId(transactionType, optionalAccount.getAccountNumber()), transactionType, transactionAmount, optionalAccount.getAccountName());
        optionalAccount.getTransaction().add(transaction);
        return transaction;
    }

    private String generateTransactionId(TransactionType transactionType, String accountNumber) {
        String transactionPrefix = switch (transactionType){
            case DEBIT-> "dbt";
            case CREDIT -> "crd";
            case TRANSFER_IN -> "tnxIn";
            case TRANSFER_OUT -> "tnxOut";
        };
        return accountNumber + transactionPrefix+ LocalDateTime.now();
    }

    public void withDrawMoneyFrom(String customerAccountNumber, BigDecimal amountToWithdraw, int accountPin,TransactionType transactionType) {
        Optional<Account> optionalAccount =accountDatabase.findById(customerAccountNumber);
        if(optionalAccount.isPresent()){
            optionalAccount.get().verifyLegibilityForWithdraw(amountToWithdraw, accountPin);
        }
        else{
            throw new WithdrawFailedException("Account not found");
        }
        notifyCustomerViaSms(saveTransaction(optionalAccount.get(), transactionType, amountToWithdraw), optionalAccount.get());
    }

    public void transfer(TransferRequest transferRequest) throws BankingApplicationException{
        Optional<Account> senderAccount = accountDatabase.findById(transferRequest.getSenderAccountNumber());
        Optional<Account> receiverAccount = accountDatabase.findById(transferRequest.getReceiverAccountNumber());
        if(senderAccount.isPresent() && receiverAccount.isPresent()){
            withDrawMoneyFrom(transferRequest.getSenderAccountNumber(), transferRequest.getAmountToTransfer(), transferRequest.getSenderAccountPin(), TRANSFER_OUT);
            depositMoneyIntoAccount(transferRequest.getAmountToTransfer(), transferRequest.getReceiverAccountNumber(), TRANSFER_IN);
        }
    }

    public void transfer(TransferRequest transferRequest, String receivingBankShortName) {
        CentralBank centralBank = CentralBank.createCentralBank();
        transferRequest.setReceiverBank(receivingBankShortName);
        transferRequest.setSenderBank(this);
        centralBank.transferFundsWith(transferRequest);
    }

    public void depositMoneyIntoAccount(BigDecimal valueOf, String accountNumber) {
        depositMoneyIntoAccount(valueOf, accountNumber, CREDIT);
    }

    public void withDrawMoneyFrom(String accountNumber, BigDecimal valueOf, int i) {
        withDrawMoneyFrom(accountNumber,valueOf, i,DEBIT);
    }

    @Override
    public String toString(){
        StringBuilder bankToString = new StringBuilder();
        bankToString.append("Bank Name: ").append(bankFullName).append("\n");
        bankToString.append("Bank Short Name: ").append(bankShortName).append("\n");
        bankToString.append("Bank Code: ").append(bankCode).append("\n");

        return bankToString.toString();
    }

}
