package com.atmmachine.repository;

import com.atmmachine.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;

public interface AccountRepository extends JpaRepository<Account,String> {

    @Query("UPDATE Account SET balance = :balance WHERE accountNumber = :accountNumber")
    @Modifying
    @Transactional
    public void updateAccountBalance(@Param("accountNumber") String accountNumber, @Param("balance") BigDecimal balance);
}
