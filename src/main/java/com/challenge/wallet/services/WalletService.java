package com.challenge.wallet.services;

import com.challenge.wallet.domain.Balance;
import com.challenge.wallet.domain.Wallet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WalletService {

    Wallet createWallet(Long userId, String walletName) throws Exception;

    Balance getCurrentBalance(Long walletId) throws Exception;

    Balance getHistoricalBalance(Long walletId, String date) throws Exception;

    Wallet deposit(Long walletId, Double amount) throws Exception;

    Wallet withdraw(Long walletId, Double amount) throws Exception;

    List<Wallet> getAll(Long userId);

    List<Wallet> transfer(Long originWalletId, Long targetWalletId, Double amount) throws Exception;
}
