package com.chalenge.wallet.services.impl;

import com.chalenge.wallet.domain.Balance;
import com.chalenge.wallet.domain.Wallet;
import com.chalenge.wallet.repositories.BalanceRepository;
import com.chalenge.wallet.services.BalanceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Override
    public Balance findByDateAndWallet(Long walletId, LocalDateTime date) {
        return balanceRepository.findByCreatedOnAndWallet_Id(date, walletId);
    }

    @Override
    @Transactional
    public Balance createBalance(Wallet wallet) {

        Balance balance = new Balance();
        balance.setFunds(wallet.getFunds());
        balance.setWallet(wallet);

        return balanceRepository.save(balance);
    }

    @Override
    public Balance findCurrentByWalletId(Long walletId) {
        return balanceRepository.findCurrentByWalletId(walletId);
    }
}
