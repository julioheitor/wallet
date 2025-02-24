package com.challenge.wallet;

import com.challenge.wallet.dtos.BalanceDTO;
import com.challenge.wallet.dtos.UserDTO;
import com.challenge.wallet.dtos.WalletDTO;
import com.challenge.wallet.repositories.BalanceRepository;
import com.challenge.wallet.repositories.UserRepository;
import com.challenge.wallet.repositories.WalletRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletApplicationTests {

	@Autowired
	private WebApplicationContext context;

	public MockMvc mockMvc;

	@Autowired
	private Gson gson;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private BalanceRepository balanceRepository;


	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@AfterEach
	public void afterTests(){
		balanceRepository.deleteAll();
		walletRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void tests() throws Exception {

		/**
		 * Test with valid arguments
		 */
		UserDTO user = testUserCreation();

		WalletDTO originWallet = testWalletCreation(user, "Wallet 1",
				(wallet) -> wallet.id() != null);

		WalletDTO targetWallet = testWalletCreation(user, "Wallet 2", (wallet) -> wallet.id() != null);

		originWallet = testDepositOperation(originWallet, 100d, status().isOk(), (wallet) -> wallet.funds() == 100d);

		originWallet = testWithdrawOperation(originWallet, 50d, status().isOk(), (wallet) -> wallet.funds() == 50d);

		testTransferOperation(originWallet, targetWallet, 50d, status().isOk(), (wallets) -> wallets.get(0).funds() == 0d && wallets.get(1).funds() == 50d);

		/**
		 * Tests with invalid arguments
		 */
		testBalanceByDateOperation(originWallet, LocalDateTime.now(), status().isBadRequest(), (balance) -> true);

		testDepositOperation(originWallet, -100d, status().isBadRequest(), (wallet) -> true);

		testWithdrawOperation(originWallet, -50d, status().isBadRequest(), (wallet) -> true);

		testTransferOperation(originWallet, targetWallet, -50d, status().isBadRequest(), (wallets) -> true);

		testWithdrawOperation(originWallet, 9999999d, status().isBadRequest(), (wallet) -> true);

	}

	private void testBalanceByDateOperation(WalletDTO wallet, LocalDateTime date, ResultMatcher matcher,
											Function<BalanceDTO, Boolean> assertion) throws Exception {

		BalanceDTO balance = gson.fromJson(mockMvc.perform(MockMvcRequestBuilders.get("/wallet/historical_balance")
						.param("walletId", String.valueOf(wallet.id()))
						.param("date", date.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.ALL_VALUE))
				.andExpect(matcher)
				.andReturn().getResponse().getContentAsString(), BalanceDTO.class);

		Assertions.assertTrue(assertion.apply(balance));
	}

	private UserDTO testUserCreation() throws Exception {

		UserDTO user = gson.fromJson(mockMvc.perform(MockMvcRequestBuilders.post("/user/create")
						.param("name", "User 1")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.ALL_VALUE))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), UserDTO.class);

		Assertions.assertTrue(user.id() != null && user.name().equals("User 1"));

		return user;
	}

	private WalletDTO testWalletCreation(UserDTO user, String name, Function<WalletDTO, Boolean> assertion) throws Exception {

		WalletDTO wallet = gson.fromJson(mockMvc.perform(MockMvcRequestBuilders.post("/wallet/create")
						.param("userId", String.valueOf(user.id()))
						.param("name", name)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.ALL_VALUE))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString(), WalletDTO.class);

		Assertions.assertTrue(assertion.apply(wallet));

		return wallet;
	}

	private WalletDTO testDepositOperation(WalletDTO wallet, Double amount, ResultMatcher matcher, Function<WalletDTO, Boolean> assertion) throws Exception {

		wallet = gson.fromJson(mockMvc.perform(MockMvcRequestBuilders.put("/wallet/deposit")
						.param("walletId", String.valueOf(wallet.id()))
						.param("amount", String.valueOf(amount))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.ALL_VALUE))
				.andExpect(matcher)
				.andReturn().getResponse().getContentAsString(), WalletDTO.class);

		Assertions.assertTrue(assertion.apply(wallet));

		return wallet;
	}

	private WalletDTO testWithdrawOperation(WalletDTO wallet, Double amount, ResultMatcher matcher, Function<WalletDTO, Boolean> assertion) throws Exception {

		wallet = gson.fromJson(mockMvc.perform(MockMvcRequestBuilders.post("/wallet/withdraw")
						.param("walletId", String.valueOf(wallet.id()))
						.param("amount", String.valueOf(amount))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.ALL_VALUE))
				.andExpect(matcher)
				.andReturn().getResponse().getContentAsString(), WalletDTO.class);

		Assertions.assertTrue(assertion.apply(wallet));

		return wallet;
	}

	private void testTransferOperation(WalletDTO originWallet, WalletDTO targetWallet, Double amount, ResultMatcher matcher,
									   Function<List<WalletDTO>, Boolean> assertion) throws Exception {

		JsonArray wallets = gson.fromJson(mockMvc.perform(MockMvcRequestBuilders.put("/wallet/transfer")
						.param("originWalletId", String.valueOf(originWallet.id()))
						.param("targetWalletId", String.valueOf(targetWallet.id()))
						.param("amount", String.valueOf(amount))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.ALL_VALUE))
				.andExpect(matcher)
				.andReturn().getResponse().getContentAsString(), JsonArray.class);

		List<WalletDTO> walletsResult = wallets != null? StreamSupport.stream(wallets.spliterator(), false).map(w -> {
			return gson.fromJson(w.toString(), WalletDTO.class);
		}).collect(Collectors.toList()) : null;

		Assertions.assertTrue(assertion.apply(walletsResult));
	}
}
