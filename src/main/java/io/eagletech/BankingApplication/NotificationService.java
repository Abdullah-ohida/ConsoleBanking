package io.eagletech.BankingApplication;

public interface NotificationService {

    Alert createAlert(Account chibuzoAccount, Transaction transaction);

    void notify(Alert alert);
}
