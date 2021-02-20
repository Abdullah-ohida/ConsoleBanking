package io.eagletech.bankingApplication;

import io.eagletech.bankingApplication.dtos.requestModels.TransferRequest;
import io.eagletech.bankingApplication.exceptions.BankingApplicationException;
import io.eagletech.bankingApplication.exceptions.DepositFailedException;
import io.eagletech.bankingApplication.exceptions.WithdrawFailedException;
import io.eagletech.bankingApplication.models.*;
import io.eagletech.bankingApplication.notification.Alert;
import io.eagletech.bankingApplication.notification.NotificationService;
import io.eagletech.bankingApplication.notification.SmsNotification;
import org.junit.jupiter.api.*;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import  static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankingApplicationTests {
    static CentralBank centralBankOfNigeria;
    static Bank gtBank;
    static Bank firstBank;
    @BeforeAll
    static void startAllTestsWithThis(){
       centralBankOfNigeria = CentralBank.createCentralBank();
        gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GTB");
        firstBank = centralBankOfNigeria.registerNewBank("First Bank of Nigeria PLC", "FBN");

    }

    @AfterAll
    static void doThisAfterEachTest(){
        centralBankOfNigeria = null;
        gtBank = null;
        firstBank = null;

    }

    @Test
    void bank_canBeCreated(){
        Bank gtBank = new Bank("Guarantee Trust Bank PLC", "gtb", "058");
        assertThat(gtBank, is(notNullValue()));
        String expectedBankToString = """
                Bank Name: Guarantee Trust Bank PLC
                Bank Short Name: gtb
                Bank Code: 058
                """;
        assertThat(gtBank.toString(), is(expectedBankToString));
    }

    @Test
    void centralBank_canBeCreated(){
        assertThat(centralBankOfNigeria, is(notNullValue()));
        CentralBank centralBank2 = CentralBank.createCentralBank();
        CentralBank centralBank3 = CentralBank.createCentralBank();
        assertThat(centralBank3,is(centralBank2));

    }

    @Test
    void centralBank_canFindBankByBankCode(){
       gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        assertThat(centralBankOfNigeria.findBankByBankCode(gtBank.getBankCode()),is(gtBank));
    }

    @Test
    void centralBank_canThrowException_whenBankIsNotFound(){
        assertThrows(BankingApplicationException.class, ()-> centralBankOfNigeria.findBankByBankCode("029384"));
    }


    @Test
    void onlyCentralBank_canCreateBank(){
        assertThat(centralBankOfNigeria.validate(gtBank), is(true));
        System.out.println(gtBank);
        Bank firstBank = new Bank("First Bank PLC", "FBN", "000399");
        assertThat(centralBankOfNigeria.validate(firstBank), is(false));
    }

    @Test
    void banks_getUniqueSixDigitBankCode_afterCreation(){
        assertThat(centralBankOfNigeria.validate(gtBank), is(true));
        assertThat(gtBank.getBankCode(), is(notNullValue()));
        assertThat(gtBank.getBankCode().length(), is(6));
    }

    @Test
    void banks_closeCustomerAccount(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        String customerAccountNumber = customer.getMyAccount().get(0).getAccountNumber();
        Account customerAccount = customer.getMyAccount().get(0);
        gtBank.closeAccountFor(customer, customerAccountNumber);
        assertThat(gtBank.getRegisteredCustomers().contains(customerAccount), is(false));
    }


    @Test
    void banks_canRegisterCustomers(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.CURRENT);
        assertThat(gtBank.getRegisteredCustomers().contains(customer.getMyAccount().get(0)), is(true));

    }

    @Test
    void customer_canOnlyCloseHisOwnAccount(){
        Customer chibuzo = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(chibuzo, AccountType.SAVINGS);

        Customer dozie = new Customer("Dozie", "Mongo", "Semicolon Village");
        gtBank.register(chibuzo, AccountType.KIDDIES);
        String chibuzoAccountNumber = chibuzo.getMyAccount().get(0).getAccountNumber();
      assertThrows(BankingApplicationException.class,()-> gtBank.closeAccountFor(dozie, chibuzoAccountNumber));

    }

    @Test
    void customers_getBvnWhenHeRegistersForFirstAccount(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        assertThat(customer.getBvn(), is(nullValue()));
        gtBank.register(customer, AccountType.SAVINGS);
        assertThat(customer.getBvn(), is(notNullValue()));

        assertThat(customer.getBvn().length(), is(10));

    }
    @Test
    void cbn_canVerifyCreatedBVN(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        String customerBvn = customer.getBvn();
        assertThat(customerBvn, is(nullValue()));
        gtBank.register(customer, AccountType.SAVINGS);
        customerBvn = customer.getBvn();

        assertThat(centralBankOfNigeria.validate(customerBvn), is(true));
        assertThat(centralBankOfNigeria.validate("99388472989874"), is(false));
    }

    @Test
    void customer_maintainsOneBvn_acrossMultipleBanks(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        customer.getMyAccount().get(0).updatePin(0, 1111);
        String customerBvn = customer.getBvn();
        firstBank.register(customer, AccountType.CURRENT);
        String customerBvnAfterSecondAccount = customer.getBvn();
        assertThat(customerBvnAfterSecondAccount, is(customerBvn));
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(4500), customer.getMyAccount().get(0).getAccountNumber());
        System.out.println(customer);
        gtBank.withDrawMoneyFrom(customer.getMyAccount().get(0).getAccountNumber(), BigDecimal.valueOf(3500), 1111);
        System.out.println(customer);
    }

    @Test
    void customer_getsBankSpecificAccountNumber_whenHeCreatesAnAccount(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        String customerGtBankAccountNumber = customer.getMyAccount().get(0).getAccountNumber();
        firstBank.register(customer, AccountType.KIDDIES);
        String customerFirstBankAccountNumber = customer.getMyAccount().get(1).getAccountNumber();
        assertThat(customerGtBankAccountNumber.length(), is(10));
        assertThat(customerFirstBankAccountNumber, is(not(customerGtBankAccountNumber)));
        System.out.println(customer);
    }

    @Test
    void customer_canDepositMoneyFromBank_WithTheirAccountNumber(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        String customerAccountNumber = customerAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber);
        assertThat(customerAccount.calculateAccountBalance(), is(BigDecimal.valueOf(1000)));
    }


    @Test
    void customer_canDepositMoney_inTheirAccountBank(){

        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        String customerAccountNumber = customerAccount.getAccountNumber();
        assertThrows(DepositFailedException.class, ()->firstBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber));

        assertThat(customerAccount.calculateAccountBalance(), is(BigDecimal.valueOf(0)));
    }


    @Test
    void customer_canWithdrawMoney_inTheirAccountBank(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        customerAccount.updatePin(0, 1111);
        String customerAccountNumber = customerAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber);
        gtBank.withDrawMoneyFrom(customerAccountNumber, BigDecimal.valueOf(400), 1111);

        assertThat(customerAccount.calculateAccountBalance(), is(BigDecimal.valueOf(600)));
    }



    @Test
    void customer_cantWithdrawMoney_ifPinIsNotSet(){

        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        String customerAccountNumber = customerAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber);
        assertThrows(WithdrawFailedException.class, ()->gtBank.withDrawMoneyFrom(customerAccountNumber, BigDecimal.valueOf(400), 1111));

        assertThat(customerAccount.calculateAccountBalance(), is(BigDecimal.valueOf(1000)));
    }


    @Test
    void customer_cantWithdrawMoney_ifPinIsWrong(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        customerAccount.updatePin(0, 1111);
        String customerAccountNumber = customerAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber);
        assertThrows(WithdrawFailedException.class, ()->gtBank.withDrawMoneyFrom(customerAccountNumber, BigDecimal.valueOf(400), 1234));

        assertThat(customerAccount.calculateAccountBalance(), is(BigDecimal.valueOf(1000)));
    }

    @Test
    void customer_cantWithdrawMoney_ifAccountNumberIsWrong(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        customerAccount.updatePin(0, 1111);
        String customerAccountNumber = customerAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber);
        assertThrows(WithdrawFailedException.class, ()->gtBank.withDrawMoneyFrom("2394839283", BigDecimal.valueOf(400), 1234));
        assertThat(customerAccount.calculateAccountBalance(), is(BigDecimal.valueOf(1000)));
    }
    @Test
    void customer_cantWithdrawMoney_ifAccountIsLow(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        customerAccount.updatePin(0, 1111);
        String customerAccountNumber = customerAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber);
        assertThrows(WithdrawFailedException.class, ()->gtBank.withDrawMoneyFrom(customerAccountNumber, BigDecimal.valueOf(1200), 1111));
    }

    @Test
    void transferRequest_canBeCreated(){
        TransferRequest transferRequest = new TransferRequest(BigDecimal.valueOf(100), "1827364635", "1621625123", 1322);
        assertThat(transferRequest.getAmountToTransfer(), is(BigDecimal.valueOf(100)));
        assertThat(transferRequest.getReceiverAccountNumber(), is("1621625123"));
        assertThat(transferRequest.getSenderAccountNumber(), is("1827364635"));
        assertThat(transferRequest.getSenderAccountPin(), is(1322));
    }

    @Test
    void customers_CanDoIntraBankTransfer(){
        Customer chibuzo = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(chibuzo, AccountType.SAVINGS);
        Account senderAccount = chibuzo.getMyAccount().get(0);
        String chibuzoAccountNumber = senderAccount.getAccountNumber();
        senderAccount.updatePin(0, 1111);
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000),chibuzoAccountNumber);


        Customer dozie = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(dozie, AccountType.SAVINGS);
        String dozieAccountNumber = dozie.getMyAccount().get(0).getAccountNumber();
        dozie.getMyAccount().get(0).updatePin(0,1322);

        gtBank.transfer(new TransferRequest(BigDecimal.valueOf(100), chibuzoAccountNumber, dozieAccountNumber, 1111));
        assertThat(dozie.getMyAccount().get(0).calculateAccountBalance(), is(BigDecimal.valueOf(100)));
        assertThat(chibuzo.getMyAccount().get(0).calculateAccountBalance(), is(BigDecimal.valueOf(900)));

        gtBank.transfer(new TransferRequest(BigDecimal.valueOf(100), dozieAccountNumber, chibuzoAccountNumber, 1322));

        assertThat(dozie.getMyAccount().get(0).calculateAccountBalance(), is(BigDecimal.valueOf(0)));
        assertThat(chibuzo.getMyAccount().get(0).calculateAccountBalance(), is(BigDecimal.valueOf(1000)));
    }

    @Test
    void customers_getAListOfTransactionOnSuccessfulTransaction(){
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer, AccountType.SAVINGS);
        Account customerAccount = customer.getMyAccount().get(0);
        customerAccount.updatePin(0, 1111);
        String customerAccountNumber = customerAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(1000), customerAccountNumber);

        assertThat(customerAccount.getTransaction().size(), is(1));

        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(2000), customerAccountNumber);

        assertThat(customerAccount.getTransaction().size(), is(2));

        gtBank.withDrawMoneyFrom(customerAccountNumber, BigDecimal.valueOf(1200), 1111);

        assertThat(customerAccount.getTransaction().size(), is(3));

    }

    @Test
    void customers_canTransferFunds_viaCbn() {
        Customer chibuzo = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(chibuzo, AccountType.SAVINGS);
        Account chibuzoAccount = chibuzo.getMyAccount().get(0);
        chibuzoAccount.updatePin(0, 1111);
        String chibuzoAccountNumber = chibuzoAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(2500), chibuzoAccountNumber);

        Customer dozie = new Customer("Dozie", "Cohort 5", "Semicolon Village");
        firstBank.register(dozie, AccountType.SAVINGS);
        Account dozieAccount = dozie.getMyAccount().get(0);
        String dozieAccountNumber = dozieAccount.getAccountNumber();

        gtBank.transfer(new TransferRequest(BigDecimal.valueOf(1000), chibuzoAccountNumber, dozieAccountNumber, 1111), "fbn");

        assertThat(dozieAccount.calculateAccountBalance(), is(BigDecimal.valueOf(1000)));
        assertThat(chibuzoAccount.calculateAccountBalance(), is(BigDecimal.valueOf(1500)));

    }

    @Test
    void notificationService_canCreateAlert_FromCompletedTransaction(){
        Customer chibuzo = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(chibuzo, AccountType.SAVINGS);
        Account chibuzoAccount = chibuzo.getMyAccount().get(0);
        chibuzoAccount.updatePin(0, 1111);
        String chibuzoAccountNumber = chibuzoAccount.getAccountNumber();
        gtBank.depositMoneyIntoAccount(BigDecimal.valueOf(2500), chibuzoAccountNumber);

        Transaction transaction = chibuzoAccount.getTransaction().get(0);
        NotificationService notifier = new SmsNotification();
        Alert alert = notifier.createAlert(chibuzoAccount, transaction);
        assertThat(alert, is(notNullValue()));
        System.out.println(alert);

    }



}
