package com.challenge.wallet.services;

import com.challenge.wallet.domain.Balance;
import com.challenge.wallet.domain.Wallet;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface BalanceService {

    Balance findByDateAndWallet(Long walletId, LocalDateTime date);

    Balance createBalance(Wallet wallet);

    Balance findCurrentByWalletId(Long walletId);
}
