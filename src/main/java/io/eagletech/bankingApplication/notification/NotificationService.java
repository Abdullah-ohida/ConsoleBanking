package io.eagletech.bankingApplication.notification;

import io.eagletech.bankingApplication.Account;
import io.eagletech.bankingApplication.models.Transaction;

public interface NotificationService {

    Alert createAlert(Account chibuzoAccount, Transaction transaction);

    void notify(Alert alert);
}
