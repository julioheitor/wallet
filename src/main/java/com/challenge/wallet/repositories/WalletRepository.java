package com.challenge.wallet.repositories;

import com.challenge.wallet.domain.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    List<Wallet> findByUser_Id(Long userId);
}
