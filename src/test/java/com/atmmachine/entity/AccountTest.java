package com.atmmachine.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {

    private Account account;

    @Before
    public void setupAccount() {
        account = new Account();
        account.setAccountNumber("4561");
        account.setBalance(new BigDecimal("200"));
        account.setPin("4561");
        account.setOverdraft(new BigDecimal("200"));
    }

    @Test
    public void testGettersSetters() {
        Assert.assertEquals("4561", account.getAccountNumber());
        Assert.assertEquals(new BigDecimal("200"), account.getBalance());
        Assert.assertEquals("4561", account.getPin());
        Assert.assertEquals(new BigDecimal("200"), account.getOverdraft());
        Assert.assertNull(account.getAuthorities());
        Assert.assertEquals("4561", account.getPassword());
        Assert.assertEquals("4561", account.getUsername());
        Assert.assertTrue(account.isAccountNonExpired());
        Assert.assertTrue(account.isAccountNonLocked());
        Assert.assertTrue(account.isEnabled());
        Assert.assertTrue(account.isCredentialsNonExpired());
    }

}
