package com.chalenge.wallet.services;

import com.chalenge.wallet.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getUser(Long id) throws Exception;

    User createUser(String name) throws Exception;
}
