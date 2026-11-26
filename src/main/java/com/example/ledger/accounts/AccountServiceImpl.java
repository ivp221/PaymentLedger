package com.example.ledger.accounts;

import com.example.ledger.platform.error.ApiException;
import com.example.ledger.platform.error.ErrorCode;
import com.example.ledger.platform.id.UUIDv7;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
class AccountServiceImpl implements AccountService{
    private final AccountRepository repo;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.repo = accountRepository;
    }

    @Override
    public Account create(NewAccount command) {
        UUID id = UUIDv7.next();
        repo.insert(id,command);
        return repo.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Account get(UUID id) throws AccountNotFoundException {
        return repo.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public Optional<Account> find(UUID id) {
        return repo.findById(id);
    }

    @Override
    public Account changeStatus(UUID id, AccountStatus target) {
        Account account = repo.findByIdForUpdate(id).orElseThrow();
        if (account.status() == target) return account;
        boolean legal = switch (account.status()){
            case ACTIVE -> target == AccountStatus.CLOSED || target == AccountStatus.FROZEN;
            case CLOSED -> target == AccountStatus.FROZEN;
            case FROZEN -> false;
        };
        if (!legal){
            throw new ApiException(ErrorCode.CONFLICT, "Illegal account status change from " + account.status() + " to " + target);
        }
        if (target == AccountStatus.CLOSED && (account.heldAmount()!= 0 || account.availableBalance()!= 0)){
            throw new ApiException(ErrorCode.CONFLICT, "Account cannot be closed without both account balance and held amount being zero");
        }
        repo.updateStatus(id,target);
        return repo.findById(id).orElseThrow();

    }
}
