package io.eagletech.BankingApplication;

public class SmsNotification implements NotificationService {
    @Override
    public Alert createAlert(Account chibuzoAccount, Transaction transaction) {
        Alert alert = new SmsAlert(chibuzoAccount, transaction);
        return alert;
    }

    @Override
    public void notify(Alert alert) {
        System.out.println(alert);
    }
}
