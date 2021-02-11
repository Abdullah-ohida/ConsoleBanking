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






}
