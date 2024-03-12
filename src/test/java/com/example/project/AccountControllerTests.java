package com.example.project;

import com.example.project.controller.AccountController;
import com.example.project.controller.AlbumController;
import com.example.project.model.account.Account;
import com.example.project.model.user.User;
import com.example.project.repository.AccountRepository;
import com.example.project.service.AccountService;
import com.example.project.service.AlbumService;
import com.example.project.service.impl.AccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTests {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetAccounts() throws Exception {
        List<Account> accounts = new ArrayList<>();
        accounts.add(getTestAccount());
        when(accountService.getAccounts()).thenReturn(accounts);
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("testUser")))
                .andExpect(jsonPath("$[0].password", Matchers.equalTo("password")))
                .andExpect(jsonPath("$[0].roles", Matchers.equalTo("roles")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testCreateAccount() throws Exception {
        Account account = getTestAccount();
        when(accountService.createAccount(any())).thenReturn(account);
        String json = objectMapper.writeValueAsString(account);
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is("testUser")));
    }

    private Account getTestAccount() {
        Account account = new Account();
        account.setPassword("password");
        account.setName("testUser");
        account.setRoles("roles");
        return account;
    }

}
