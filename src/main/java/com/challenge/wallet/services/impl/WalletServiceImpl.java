package com.challenge.wallet.services.impl;

import com.challenge.wallet.domain.Balance;
import com.challenge.wallet.domain.User;
import com.challenge.wallet.domain.Wallet;
import com.challenge.wallet.exceptions.NegativeAmountException;
import com.challenge.wallet.exceptions.NoBalanceFoundException;
import com.challenge.wallet.exceptions.NotEnoughFundsException;
import com.challenge.wallet.repositories.WalletRepository;
import com.challenge.wallet.services.BalanceService;
import com.challenge.wallet.services.UserService;
import com.challenge.wallet.services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class WalletServiceImpl implements WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @Override
    public Wallet createWallet(Long userId, String walletName) throws Exception {

        User user = userService.getUser(userId);

        Wallet wallet = new Wallet();
        wallet.setName(walletName);
        wallet.setUser(user);
        wallet.setFunds(0d);

        return walletRepository.save(wallet);
    }

    @Override
    public Balance getCurrentBalance(Long walletId) throws Exception {
        logger.info("Getting current balance. WalletId: " + walletId);
        return balanceService.findCurrentByWalletId(walletId);
    }

    @Override
    public Balance getHistoricalBalance(Long walletId, String date) throws Exception {

        logger.info("Getting historical balance. WalletId: " + walletId + " date: " + date);

        LocalDateTime d = LocalDateTime.parse(date);

        Balance balance = balanceService.findByDateAndWallet(walletId, d);

        if(balance == null){
            throw new NoBalanceFoundException("WalletId: " + walletId + " date: " + date);
        }

        return balanceService.findByDateAndWallet(walletId, d);
    }

    @Override
    @Transactional
    public Wallet deposit(Long walletId, Double amount) throws Exception {

        logger.info("Deposit operation. WalletId: " + walletId + " amount: " + amount);

        if(amount <= 0){
            throw new NegativeAmountException("WalletId: " + walletId + " amount: " + amount);
        }

        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new Exception("Wallet not found."));
        wallet.setFunds(wallet.getFunds() + amount);

        wallet = walletRepository.save(wallet);
        Balance balance = balanceService.createBalance(wallet);

        return wallet;
    }

    @Override
    @Transactional
    public Wallet withdraw(Long walletId, Double amount) throws Exception {

        logger.info("Withdraw operation. WalletId: " + walletId + " amount: " + amount);

        if(amount <= 0){
            throw new NegativeAmountException("WalletId: " + walletId + " amount: " + amount);
        }

        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new Exception("Wallet not found."));

        if(wallet.getFunds() < amount){
            throw new NotEnoughFundsException("WalletId: " + walletId + " amount: " + amount);
        }

        wallet.setFunds(wallet.getFunds() - amount);

        wallet = walletRepository.save(wallet);
        balanceService.createBalance(wallet);

        return wallet;
    }

    @Override
    public List<Wallet> getAll(Long userId) {
        return walletRepository.findByUser_Id(userId);
    }

    @Override
    @Transactional
    public List<Wallet> transfer(Long originWalletId, Long targetWalletId, Double amount) throws Exception {

        logger.info("Tranfer operation. OriginWalletId: " + originWalletId + ". TargerWalletId: " + targetWalletId + ". amount: " + amount);

        if(amount <= 0){
            throw new NegativeAmountException("Amount: " + amount);
        }

        Wallet originWallet = walletRepository.findById(originWalletId).
                orElseThrow(() -> new Exception("Wallet not found: " + originWalletId));

        Wallet targetWallet = walletRepository.findById(targetWalletId).
                orElseThrow(() -> new Exception("Wallet not found: " + targetWalletId));

        if(originWallet.getFunds() < amount){
            throw new NotEnoughFundsException("WalletId: " + originWallet + " funds: " + originWallet.getFunds());
        }

        originWallet.setFunds(originWallet.getFunds() - amount);
        originWallet = walletRepository.save(originWallet);

        targetWallet.setFunds(targetWallet.getFunds() + amount);
        targetWallet = walletRepository.save(targetWallet);

        Balance originBalance = balanceService.createBalance(originWallet);
        Balance targetBalance = balanceService.createBalance(targetWallet);

        return Arrays.asList(originWallet, targetWallet);
    }
}
