package io.eagletech.bankingApplication.models;

import io.eagletech.bankingApplication.Account;
import io.eagletech.bankingApplication.database.Database;
import io.eagletech.bankingApplication.database.DatabaseImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Customer {
    @Getter
    private String customerFirstName;
    @Getter
    private String customerLastName;
    private String customerAddress;
    private Database<Account> myAccounts;
    @Getter @Setter
    private String bvn;
    public Customer(String customerFirstName, String customerLastName, String customerAddress) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerAddress = customerAddress;
        this.myAccounts = new DatabaseImpl<>() {
        };
    }

    public void addAccount(Account account) {
        myAccounts.save(account);
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
        for(Account account: myAccounts.findAll()){
            customerProfile.append(account.toString()).append("\n\n");
        }
        return customerProfile.toString();




    }

    public List<Account> getMyAccount() {
        return myAccounts.findAll();
    }


}
