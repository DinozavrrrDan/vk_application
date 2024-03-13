package com.example.project;

import com.example.project.exeption.BusinessException;
import com.example.project.model.account.Account;
import com.example.project.repository.AccountRepository;
import com.example.project.service.AccountService;
import com.example.project.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setup() {
        accountService = new AccountServiceImpl(passwordEncoder, accountRepository);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testCreateAccount_Success() throws BusinessException {
        Account account = getTestAccount();
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertEquals("encodedPassword", createdAccount.getPassword());
    }

    @Test
    public void testCreateAccount_ThrowsBusinessException(){
        Account account = getTestAccount();
        when(accountRepository.findByName(any())).thenReturn(Optional.of(account));

        assertThrows(BusinessException.class, () -> {
           accountService.createAccount(account);
        });
    }


    @Test
    public void testGetAccounts_Success() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(getTestAccount());
        accounts.add(getTestAccount());
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> retrievedAccounts = accountService.getAccounts();

        assertEquals(accounts, retrievedAccounts);
    }

    @Test
    public void testDeleteAccount_ThrowsBusinessException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        Account testAccount = getTestAccount();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        assertThrows(BusinessException.class, () -> {
            accountService.deleteAccount("1");
        });
    }

    @Test
    public void testDeleteAccount_Success() throws BusinessException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        Account testAccount = getTestAccount();
        when(authentication.getName()).thenReturn("testUser2");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        accountService.deleteAccount("1");
    }

    private Account getTestAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setPassword("password");
        account.setName("testUser");
        return account;
    }

}
