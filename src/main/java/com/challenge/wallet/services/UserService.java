package com.challenge.wallet.services;

import com.challenge.wallet.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getUser(Long id) throws Exception;

    User createUser(String name) throws Exception;
}
