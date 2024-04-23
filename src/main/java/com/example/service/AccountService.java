package com.example.service;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void registerUser(Account account) {
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new DuplicateKeyException("Username already exists");
        }
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        Account acc = new Account(account.getUsername(), account.getPassword());
        accountRepository.save(acc);
    }

    public Account loginUser(String username, String password) {
        Account account = accountRepository.findByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }



}