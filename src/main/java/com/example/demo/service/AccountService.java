package com.example.demo.service;



import com.example.demo.dto.AccountDto;
import com.example.demo.model.Account;
import com.example.demo.model.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Transactional
    public List<Account> transfer(AccountDto accountDto, long id) {

        User currentUser = userDetailsServiceImpl.getCurrentUserName();
        User ownerUser = userRepository.findById(accountDto.getFromID()).orElseThrow();

        if (!Objects.equals(currentUser, ownerUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (accountDto.getAmount().signum() != 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (accountDto.getFromID() == accountDto.getToID()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Account from = accountRepository.findById(accountDto.getFromID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        Account to = accountRepository.findById(accountDto.getToID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        if (from.getBalance().subtract(accountDto.getAmount()).signum() == -1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        BigDecimal fromNewBalance = from.getBalance().subtract(accountDto.getAmount());
        BigDecimal toNewBalance = to.getBalance().add(accountDto.getAmount());
        from.setBalance(fromNewBalance);
        accountRepository.save(from);
        to.setBalance(toNewBalance);
        accountRepository.save(to);
        List<Account> accounts = new ArrayList<>();
        accounts.add(from);
        accounts.add(to);

        return accounts;
    }

    public Account getAccount(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account not found"));
    }

    public List<Account> getAccounts() {

        return accountRepository.findAll();
    }

}

