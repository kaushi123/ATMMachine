package com.atmmachine.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class WithdrawalResponseDTOTest {

    private WithdrawalResponseDTO withdrawalResponseDTO;

    @Before
    public void setup(){
        withdrawalResponseDTO = new WithdrawalResponseDTO();
        withdrawalResponseDTO.setMessage("Insufficient Balance");
        withdrawalResponseDTO.setOverdraft(BigDecimal.valueOf(150));

    }

    @Test
    public void testGettersSetters(){
        Assert.assertEquals("Insufficient Balance",withdrawalResponseDTO.getMessage());
        Assert.assertEquals(BigDecimal.valueOf(150),withdrawalResponseDTO.getOverdraft());

    }

}
