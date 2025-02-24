package com.chalenge.wallet.repositories;

import com.chalenge.wallet.domain.Balance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface BalanceRepository extends CrudRepository<Balance, Long> {

    Balance findByCreatedOnAndWallet_Id(LocalDateTime date, Long walletId);

    @Query("select b from Balance b where b.createdOn in " +
            "(select max(bal.createdOn) from Balance bal where bal.wallet.id = :walletId)")
    Balance findCurrentByWalletId(Long walletId);
}
