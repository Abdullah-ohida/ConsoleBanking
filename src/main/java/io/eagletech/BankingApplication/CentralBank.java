package io.eagletech.BankingApplication;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CentralBank {
    private final Storable<Bank> registeredBanks;
    private final Map<String, Customer> bvnDatabase;

    private CentralBank(){
          registeredBanks = new Database<>();
          bvnDatabase = new ConcurrentHashMap<>();
    }

    public Bank registerNewBank(String bankFullName, String bankShortName) {
          String uniqueBankNumber = generateUniqueBankNumber();
          Bank newBank = new Bank(bankFullName, bankShortName, uniqueBankNumber);
          saveNewlyCreatedBankToDatabase(newBank);
          return newBank;
    }

    private void saveNewlyCreatedBankToDatabase(Bank newBank) {
          registeredBanks.save(newBank);
    }

    private String generateUniqueBankNumber() {
          SecureRandom secureRandom = new SecureRandom();
          String uniqueBankNumber =String.format("%06d", secureRandom.nextInt(999999));
          return uniqueBankNumber;
    }

    public boolean validate(Bank banktoValidate) {
          return registeredBanks.contains(banktoValidate);
    }

    public void registerCreateBvnFor(Customer customer) {
          customer.setBvn(generateBvn());
          bvnDatabase.put(customer.getBvn(), customer);
    }

    private String generateBvn() {
          SecureRandom randomNumberGenerator = new SecureRandom();
          return ""+ randomNumberGenerator.nextInt(99_999)+ randomNumberGenerator.nextInt(99_999);

    }

    public boolean validate(String customerBvn) {
      return bvnDatabase.containsKey(customerBvn);
    }


    private static class CentralBankSingleTonHelper{
        private static final CentralBank instance = new CentralBank();
    }

    public static CentralBank createCentralBank(){
      return CentralBankSingleTonHelper.instance;
    }
}
