package com.atmmachine.service;

import com.atmmachine.entity.Account;
import com.atmmachine.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private SecurityService securityService;

    @Test
    public void loadUserByUsernameTestSuccess() {
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("4561", new BigDecimal(800))));
        assertDoesNotThrow(() -> securityService.loadUserByUsername("validAccount"));
    }

    @Test
    public void loadUserByUsernameTestFailure() {
        when(accountRepository.findById("invalidAccount")).thenReturn(Optional.empty());
        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> securityService.loadUserByUsername("invalidAccount"));
        assertEquals("No account found for given account number", exception.getMessage());
    }

}
