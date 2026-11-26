package com.example.ledger.accounts;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Account create (NewAccount command);
    Account get(UUID id) throws AccountNotFoundException;
    Optional<Account> find(UUID id);
    Account changeStatus(UUID id, AccountStatus target);
}
