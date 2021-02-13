package io.eagletech.BankingApplication;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentHashMap;

public class CentralBank {
    private final Database<Bank> registeredBanks;
    private final Map<String, Customer> bvnDatabase;

    private CentralBank(){
          registeredBanks = new DatabaseImpl<>();
          bvnDatabase = new ConcurrentHashMap<>();
    }

    public Bank registerNewBank(String bankFullName, String bankShortName) {
          String uniqueBankNumber = generateUniqueBankNumber();
          Bank newBank = new Bank(bankFullName, bankShortName, uniqueBankNumber);
          saveNewlyCreatedBankToDatabase(newBank);
          return newBank; }

    private void saveNewlyCreatedBankToDatabase(Bank newBank) {
          registeredBanks.save(newBank);
    }

    private String generateUniqueBankNumber() {
          SecureRandom secureRandom = new SecureRandom();
          String uniqueBankNumber =String.format("%06d", secureRandom.nextInt(999999));
          return uniqueBankNumber;
    }

    public boolean validate(Bank bankToValidate) {
          return registeredBanks.contains(bankToValidate);
    }

    public void registerCreateBvnFor(Customer customer) {
          customer.setBvn(generateBvn());
          bvnDatabase.put(customer.getBvn(), customer);
    }

    private String generateBvn() {
        StringBuilder generatedBvn = new StringBuilder();
        SplittableRandom randomNumberGenerator = new SplittableRandom();
        final int LENGTH_OF_BVN = 10;
        int totalNumberGenerated = 0;
        while(totalNumberGenerated < LENGTH_OF_BVN){
            int randomNumber = randomNumberGenerator.nextInt(0, 9);
            generatedBvn.append(randomNumber);
            totalNumberGenerated++;
        }
        return generatedBvn.toString();
    }

    public boolean validate(String customerBvn) {
      return bvnDatabase.containsKey(customerBvn);
    }

    public Bank findBankByBankCode(String bankCode) {

        Optional<Bank> optionalBank = registeredBanks.findById(bankCode);
        if(optionalBank.isPresent()){
            return optionalBank.get();
       }
        else{
            throw new BankingApplicationException("Bank does not exist");
        }
    }

    private static class CentralBankSingleTonHelper{
        private static final CentralBank instance = new CentralBank();
    }

    public static CentralBank createCentralBank(){
      return CentralBankSingleTonHelper.instance;
    }
}
