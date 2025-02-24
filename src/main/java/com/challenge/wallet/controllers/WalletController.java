package com.chalenge.wallet.controllers;

import com.chalenge.wallet.domain.Balance;
import com.chalenge.wallet.domain.Wallet;
import com.chalenge.wallet.dtos.BalanceDTO;
import com.chalenge.wallet.dtos.WalletDTO;
import com.chalenge.wallet.services.WalletService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/wallet")
@ExposesResourceFor(Wallet.class)
public class WalletController {

	private final Logger logger = LoggerFactory.getLogger(WalletController.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private WalletService walletService;

	@GetMapping("/all")
	public ResponseEntity<Collection<WalletDTO>> getAll(@RequestParam(required = true) Long userId){
		List<WalletDTO> walletDTOs = modelMapper.map(walletService.getAll(userId), new TypeToken<List<WalletDTO>>() {}.getType());
		return ResponseEntity.ok(walletDTOs);
	}

	@PostMapping("/create")
	public ResponseEntity<WalletDTO> create(@RequestParam(required = true) Long userId, @RequestParam(required = true) String name) throws Exception {
		WalletDTO walletDTO = modelMapper.map(walletService.createWallet(userId, name), WalletDTO.class);
		return ResponseEntity.ok(walletDTO);
	}

	@GetMapping("/current_balance")
	public ResponseEntity<BalanceDTO> getCurrentBalance(@RequestParam(required = true) Long walletId) throws Exception {
		BalanceDTO balanceDTO = modelMapper.map(walletService.getCurrentBalance(walletId), BalanceDTO.class);
		return ResponseEntity.ok(balanceDTO);
	}

	@GetMapping("/historical_balance")
	public ResponseEntity<BalanceDTO> getHistoricalBalance(@RequestParam(required = true) Long walletId,
									   @RequestParam(required = true) String date) throws Exception {
		BalanceDTO balanceDTO = modelMapper.map(walletService.getHistoricalBalance(walletId, date), BalanceDTO.class);
		return ResponseEntity.ok(balanceDTO);
	}

	@PutMapping("/deposit")
	public ResponseEntity<WalletDTO> deposit(@RequestParam(required = true) Long walletId, @RequestParam(required = true) Double amount) throws Exception {
		WalletDTO walletDTO = modelMapper.map(walletService.deposit(walletId, amount), WalletDTO.class);
		return ResponseEntity.ok(walletDTO);
	}

	@PostMapping("/withdraw")
	public ResponseEntity<WalletDTO> withdraw(@RequestParam(required = true) Long walletId, @RequestParam(required = true) Double amount) throws Exception {
		WalletDTO walletDTO = modelMapper.map(walletService.withdraw(walletId, amount), WalletDTO.class);
		return ResponseEntity.ok(walletDTO);
	}

	@PutMapping("/transfer")
	public ResponseEntity<Collection<WalletDTO>> transfer(@RequestParam(required = true) Long originWalletId,
								 @RequestParam(required = true) Long targetWalletId,
								 @RequestParam(required = true) Double amount) throws Exception {
		List<WalletDTO> walletDTOs = modelMapper.map(walletService.transfer(originWalletId, targetWalletId, amount),
				new TypeToken<List<WalletDTO>>() {}.getType());
		return ResponseEntity.ok(walletDTOs);
	}
}
