package com.atmmachine.dto;

import com.atmmachine.entity.Account;
import com.atmmachine.entity.Notes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class WithdrawalResponseDTO {

    public List<Notes> withdrawalList;

    public WithdrawalResponseDTO() {
    }

    public WithdrawalResponseDTO(List<Notes> withdrawalList, String message, BigDecimal balance, BigDecimal overdraft) {
        this.withdrawalList = withdrawalList;
        this.message = message;
        this.balance = balance;
        this.overdraft = overdraft;
    }

    public String message;
    public BigDecimal balance;
    public BigDecimal overdraft;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.overdraft = overdraft;
    }
    public List<Notes> getWithdrawalList() {
        return withdrawalList;
    }

    public void setWithdrawalList(List<Notes> withdrawalList) {
        this.withdrawalList = withdrawalList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
