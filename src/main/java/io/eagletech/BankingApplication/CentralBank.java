package io.eagletech.BankingApplication;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class CentralBank {
    private final List<Bank> registeredBanks;
      private CentralBank(){
          registeredBanks = new ArrayList<>();
    }

    public Bank registerNewBank(String bankFullName, String bankShortName) {
          String uniqueBankNumber = generateUniqueBankNumber();
          Bank newBank = new Bank(bankFullName, bankShortName, uniqueBankNumber);
          saveNewlyCreatedBankToDatabase(newBank);
          return newBank;
    }

    private void saveNewlyCreatedBankToDatabase(Bank newBank) {
          registeredBanks.add(newBank);
    }

    private String generateUniqueBankNumber() {
          SecureRandom secureRandom = new SecureRandom();
          String uniqueBankNumber =String.format("%06d", secureRandom.nextInt(999999));
          return uniqueBankNumber;
    }

    public boolean validate(Bank gtBank) {
          return registeredBanks.contains(gtBank);
    }

    private static class CentralBankSingleTonHelper{
        private static final CentralBank instance = new CentralBank();
    }

    public static CentralBank createCentralBank(){
      return CentralBankSingleTonHelper.instance;
    }
}
