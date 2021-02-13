package io.eagletech.BankingApplication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.hamcrest.Matchers.*;
import  static org.hamcrest.MatcherAssert.*;
public class BankingApplicationTests {
    CentralBank centralBankOfNigeria;
    @BeforeEach
    void startAllTestsWithThis(){
        centralBankOfNigeria = CentralBank.createCentralBank();
    }

    @AfterEach
    void doThisAfterEachTest(){

    }

    @Test
    void bank_canBeCreated(){
        Bank gtBank = new Bank("Guarantee Trust Bank PLC", "GT Bank", "058");
        assertThat(gtBank, is(notNullValue()));

        String expectedBankToString = """
                Bank Name: Guarantee Trust Bank PLC
                Bank Short Name: GT Bank
                Bank Code: 058
                """;
        assertThat(gtBank.toString(), is(expectedBankToString));
    }

    @Test
    void centralBank_canBeCreated(){
        assertThat(centralBankOfNigeria, is(notNullValue()));
        CentralBank centralBank2 = CentralBank.createCentralBank();
        assertThat(centralBankOfNigeria,is(centralBank2));
    }

    @Test
    void onlyCentralBank_canCreateBank(){
        Bank gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        assertThat(centralBankOfNigeria.validate(gtBank), is(true));
        System.out.println(gtBank);

        Bank firstBank = new Bank("First Bank PLC", "FBN", "000399");
        assertThat(centralBankOfNigeria.validate(firstBank), is(false));
    }

    @Test
    void banks_getUniqueSixDigitBankCode_afterCreation(){
        Bank gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        assertThat(centralBankOfNigeria.validate(gtBank), is(true));
        assertThat(gtBank.getBankCode(), is(notNullValue()));
        assertThat(gtBank.getBankCode().length(), is(6));
    }

    @Test
    void banks_canRegisterCustomers(){
        Bank gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");

        gtBank.register(customer);
        assertThat(gtBank.getRegisteredCustomers().contains(customer.getMyAccount().get(0)), is(true));

    }

    @Test
    void customers_getBvnWhenHeRegistersForFirstAccount(){
        Bank gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        assertThat(customer.getBvn(), is(nullValue()));
        gtBank.register(customer);
        assertThat(customer.getBvn(), is(notNullValue()));
        System.out.println(customer.getBvn());
        assertThat(customer.getBvn().length(), is(10));

    }
    @Test
    void cbn_canVerifyCreatedBVN(){
        Bank gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        String customerBvn = customer.getBvn();
        assertThat(customerBvn, is(nullValue()));
        gtBank.register(customer);
        customerBvn = customer.getBvn();

        assertThat(centralBankOfNigeria.validate(customerBvn), is(true));
        assertThat(centralBankOfNigeria.validate("99388472989874"), is(false));
    }

    @Test
    void customer_maintainsOneBvn_acrossMultipleBanks(){
        Bank gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        Bank firstBank = centralBankOfNigeria.registerNewBank("First Bank of Nigeria", "FBN");
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer);
        String customerBvn = customer.getBvn();
        firstBank.register(customer);
        String customerBvnAfterSecondAccount = customer.getBvn();
        assertThat(customerBvnAfterSecondAccount, is(customerBvn));
        System.out.println(customer);

    }

    @Test
    void customer_getsBankSpecificAccountNumber_whenHeCreatesAnAccount(){
        Bank gtBank = centralBankOfNigeria.registerNewBank("Guarantee Trust Bank PLC", "GT Bank");
        Bank firstBank = centralBankOfNigeria.registerNewBank("First Bank of Nigeria", "FBN");
        Customer customer = new Customer("Chibuzo", "Gabriel", "Semicolon Village");
        gtBank.register(customer);
        String customerGTbankAccountNumber = customer.getMyAccount().get(0).getAccountNumber();
        firstBank.register(customer);
        String customerFirstBankAccountNumber = customer.getMyAccount().get(1).getAccountNumber();

        assertThat(customerGTbankAccountNumber.length(), is(10));
        assertThat(customerFirstBankAccountNumber, is(not(customerGTbankAccountNumber)));
        System.out.println(customer);
    }


}
