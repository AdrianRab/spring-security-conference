package com.pluralsight.conference.repository;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.model.VerificationToken;

public interface AccountRepository {
    Account create(Account account);

    void saveToken(VerificationToken verificationToken);
}
