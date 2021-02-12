package io.eagletech.BankingApplication;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    @Getter
    private String customerFirstName;
    @Getter
    private String customerLastName;
    private String customerAddress;
    private List<Account> myAccounts;
    @Getter @Setter
    private String bvn;
    public Customer(String customerFirstName, String customerLastName, String customerAddress) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerAddress = customerAddress;
        this.myAccounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        myAccounts.add(account);
    }

    @Override
    public String toString(){
        StringBuilder customerProfile= new StringBuilder();
        customerProfile.append("First Name: ").append(customerFirstName).append("\n");
        customerProfile.append("Last Name: ").append(customerLastName).append("\n");
        customerProfile.append("Address: ").append(customerAddress).append("\n\n");
        if(myAccounts.size()>0){
            customerProfile.append("My Account List\n");
        }
        for(Account account: myAccounts){
            customerProfile.append(account.toString()).append("\n\n");
        }
        return customerProfile.toString();




    }

    public List<Account> getMyAccount() {
        return myAccounts;
    }
}
