package com.chalenge.wallet.services;

import com.chalenge.wallet.domain.Balance;
import com.chalenge.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface BalanceService {

    Balance findByDateAndWallet(Long walletId, LocalDateTime date);

    Balance createBalance(Wallet wallet);

    Balance findCurrentByWalletId(Long walletId);
}
