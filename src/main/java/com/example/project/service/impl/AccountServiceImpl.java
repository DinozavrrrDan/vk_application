package com.example.project.service.impl;

import com.example.project.exeption.BusinessException;
import com.example.project.model.account.Account;
import com.example.project.repository.AccountRepository;
import com.example.project.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.project.consts.ExceptionConsts.ACCOUNT_ALREADY_EXISTS;
import static com.example.project.consts.ExceptionConsts.YOU_CAN_T_DELETE_THE_ACCOUNT_YOU_LOGGED_IN_TO;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) throws BusinessException {
        if (accountRepository.findByName(account.getName()).isPresent()) {
            throw new BusinessException(ACCOUNT_ALREADY_EXISTS);
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteAccount(String id) throws BusinessException {
        if (isAccountToDeleteIsAuthAccountOrEmptyAccount(Long.valueOf(id))) {
            throw new BusinessException(YOU_CAN_T_DELETE_THE_ACCOUNT_YOU_LOGGED_IN_TO);
        }
        accountRepository.deleteById(Long.valueOf(id));
    }

    private boolean isAccountToDeleteIsAuthAccountOrEmptyAccount(Long id) {
        return accountRepository.findById(id).isEmpty()
                || accountRepository.findById(id).get().getName()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
