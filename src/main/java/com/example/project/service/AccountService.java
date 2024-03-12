package com.example.project.service;

import com.example.project.exeption.BusinessException;
import com.example.project.model.account.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    List<Account> getAccounts();

    void deleteAccount(String id) throws BusinessException;
}
