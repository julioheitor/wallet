package com.challenge.wallet.services.impl;

import com.challenge.wallet.domain.User;
import com.challenge.wallet.repositories.UserRepository;
import com.challenge.wallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User getUser(Long id) throws Exception {
		return userRepository.findById(id).orElseThrow(() -> new Exception("Usuário inexistente"));
	}

	@Override
	public User createUser(String name) throws Exception {

		User user = userRepository.findByName(name);

		if(user != null){
			throw new IllegalArgumentException("User already exists with the name " + name);
		}

		user = new User();
		user.setName(name);

		return userRepository.save(user);
	}

}
