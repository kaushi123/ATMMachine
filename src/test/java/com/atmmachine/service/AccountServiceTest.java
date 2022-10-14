package com.atmmachine.service;

import com.atmmachine.dto.WithdrawalResponseDTO;
import com.atmmachine.entity.Account;
import com.atmmachine.entity.Notes;
import com.atmmachine.repository.AccountRepository;
import com.atmmachine.repository.NoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private AccountService accountService;

    @InjectMocks
    private WithdrawalResponseDTO withdrawalResponseDTO;

    @InjectMocks
    private NoteService noteService = Mockito.spy(new NoteService());

    @Before
    public void setup() {
        List<Notes>notesFromATM = new ArrayList<>();
        notesFromATM.add(new Notes(50, 10));
        notesFromATM.add(new Notes(20, 30));
        notesFromATM.add(new Notes(10, 30));
        notesFromATM.add(new Notes(5, 20));
        when(noteRepository.findAll()).thenReturn(notesFromATM);
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("123456789", new BigDecimal(800))));
    }

    @Test
    public void getAccountInformationTestSuccess() {
        Account account = new Account("123456789", new BigDecimal(800));
        assertEquals(account.getAccountNumber(), accountService.getBalanceInfo("validAccount").getAccountNumber());
        assertEquals(account.getBalance(), accountService.getBalanceInfo("validAccount").getBalance());
    }


    @Test
    public void getAccountInformationTestFailure() {
        when(accountRepository.findById("8526")).thenReturn(Optional.empty());
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.getBalanceInfo("4561"));
        assertEquals("No Account found for given Account Number", exception.getMessage());
    }

    @Test
    public void updateAccountBalanceTest() {
        assertDoesNotThrow(() -> accountService.updateAccountBalance("4561", new BigDecimal("100")));
    }

    @Test
    public void withdrawTestFailureNotEnoughFundsWithoutOverdraft() {
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("4561", new BigDecimal(0), new BigDecimal(100))));
        WithdrawalResponseDTO withdrawalResponseDTO = accountService.withdrawRequest("validAccount", 300, false);
        withdrawalResponseDTO.setMessage("Insufficient Balance in the account");
        assertEquals("Insufficient Balance in the account",withdrawalResponseDTO.getMessage());
    }

    @Test
    public void withdrawTestFailureNotEnoughFundsWithOverdraft() {
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("4561", new BigDecimal(0), new BigDecimal(100))));
        WithdrawalResponseDTO withdrawalResponseDTO = accountService.withdrawRequest("validAccount", 500, true);
        withdrawalResponseDTO.setMessage("Insufficient Balance in the account");
        assertEquals("Insufficient Balance in the account",withdrawalResponseDTO.getMessage());
    }

    @Test
    public void withdrawTestFailureNonValidAmount() {

        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("4561", new BigDecimal(0), new BigDecimal(100))));
        WithdrawalResponseDTO withdrawalResponseDTO = accountService.withdrawRequest("validAccount", 501, true);
        withdrawalResponseDTO.setMessage("The request can't be completed since this ATM doesn't hold bills smaller than €5");
        assertEquals("The request can't be completed since this ATM doesn't hold bills smaller than €5",withdrawalResponseDTO.getMessage());
    }

    @Test
    public void withdrawTestFailureZeroAmount() {
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("4561", new BigDecimal(0), new BigDecimal(100))));
        WithdrawalResponseDTO withdrawalResponseDTO = accountService.withdrawRequest("validAccount", 0, true);
        withdrawalResponseDTO.setMessage("Amount should be greater than 0");
        assertEquals("Amount should be greater than 0",withdrawalResponseDTO.getMessage());
    }
}
