package com.atmmachine.controller;

import com.atmmachine.dto.WithdrawalResponseDTO;
import com.atmmachine.entity.Account;
import com.atmmachine.entity.Notes;
import com.atmmachine.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping(path = "/account/balance",produces = { MediaType.APPLICATION_JSON_VALUE })
    public Account getAccountBalance(Principal principal){
        Account account = accountService.getBalanceInfo(principal.getName());
        return account;
    }

    @GetMapping(path="/account/withdrawal",produces = {MediaType.APPLICATION_JSON_VALUE})
    public WithdrawalResponseDTO withdrawalRequest(Principal principal, @RequestParam(name="amount") int amount, @RequestParam(name="allowOverDraft") boolean allowOverDraft){
        WithdrawalResponseDTO withdrawCashAmount = accountService.withdrawRequest(principal.getName(),amount,allowOverDraft);
        return withdrawCashAmount;
    }


}
