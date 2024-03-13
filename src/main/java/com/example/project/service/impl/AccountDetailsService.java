package com.example.project.service.impl;

import com.example.project.model.account.Account;
import com.example.project.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.project.consts.ExceptionConsts.NOT_FOUND;

@Service
@AllArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findByName(username);
        return user.map(AccountDetails::new).orElseThrow(() -> new UsernameNotFoundException(username + NOT_FOUND));
    }
}
