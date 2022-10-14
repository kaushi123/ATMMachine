package com.atmmachine.controller;
import com.atmmachine.dto.WithdrawalResponseDTO;
import com.atmmachine.entity.Account;
import com.atmmachine.service.AccountService;
import com.atmmachine.service.NoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteService noteService;

    @InjectMocks
    private AccountController accountController;

    @MockBean
    private AccountService accountService;

    @Before
    public void setup() {
        when(accountService.getBalanceInfo("123456789")).thenReturn(new Account("123456789", new BigDecimal(800)));
    }

    @Test
    @WithMockUser("123456789")
    public void getAccountBalanceTestSuccess() throws Exception {
        mockMvc.perform(get("/api/account/balance")
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800));
    }

    @Test
    public void getAccountBalanceTestFailureNotSignedIn() throws Exception {
        mockMvc.perform(get("/api/account/balance")
                        .accept(MediaType.ALL))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser("123456789")
    public void withdrawTestSuccess() throws Exception {
        WithdrawalResponseDTO withdrawalResponseDTO = new WithdrawalResponseDTO();
        when(accountService.withdrawRequest("123456789", 100, false)).thenReturn(withdrawalResponseDTO);
        mockMvc.perform(get("/api/account/withdrawal?amount=100&allowOverDraft=false")
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("123456789")
    public void withdrawTestFailureNoBalance() throws Exception {
        WithdrawalResponseDTO withdrawalResponseDTO = new WithdrawalResponseDTO();
        withdrawalResponseDTO.setMessage("Insufficient Balance in the account");
        when(accountService.withdrawRequest("123456789", 100, false))
                .thenReturn(withdrawalResponseDTO);
        mockMvc.perform(get("/api/account/withdrawal?amount=100&allowOverDraft=false")
                        .accept(MediaType.ALL))
                .andExpect(jsonPath("$.message").value("Insufficient Balance in the account"));
    }

    @Test
    @WithMockUser("123456789")
    public void withdrawTestFailureNoATMBalance() throws Exception {
        WithdrawalResponseDTO withdrawalResponseDTO = new WithdrawalResponseDTO();
        withdrawalResponseDTO.setMessage("Insufficient Balance in the ATM");
        when(accountService.withdrawRequest("123456789", 100, false))
                .thenReturn(withdrawalResponseDTO);
        mockMvc.perform(get("/api/account/withdrawal?amount=100&allowOverDraft=false")
                        .accept(MediaType.ALL))
                .andExpect(jsonPath("$.message").value("Insufficient Balance in the ATM"));
    }

    @Test
    @WithMockUser("123456789")
    public void withdrawTestFailureNonAllowedAmount() throws Exception {
        WithdrawalResponseDTO withdrawalResponseDTO = new WithdrawalResponseDTO();
        withdrawalResponseDTO.setMessage("The request can't be completed since this ATM doesn't hold bills smaller than €5");
        when(accountService.withdrawRequest("123456789", 138, false))
                .thenReturn(withdrawalResponseDTO);
        mockMvc.perform(get("/api/account/withdrawal?amount=138&allowOverDraft=false")
                        .accept(MediaType.ALL))
                .andExpect(jsonPath("$.message").value("The request can't be completed since this ATM doesn't hold bills smaller than €5"));
    }

}
