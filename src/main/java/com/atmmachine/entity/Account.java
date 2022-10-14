package com.atmmachine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@JsonIgnoreProperties({"pin", "enabled", "password", "username", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class Account implements UserDetails {
    @Id
    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    private String accountNumber;

    @Column(name = "PIN", nullable = false)
    private String pin;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @Column(name = "OVERDRAFT", nullable = false)
    private BigDecimal overdraft;


    public Account(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account(String accountNumber, BigDecimal balance, BigDecimal overdraft) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.overdraft = overdraft;
    }

    public Account() {
    }

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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return pin;
    }

    @Override
    public String getUsername() {
        return accountNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
