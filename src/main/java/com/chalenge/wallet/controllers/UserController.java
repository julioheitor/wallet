package com.chalenge.wallet.controllers;

import com.chalenge.wallet.domain.User;
import com.chalenge.wallet.dtos.UserDTO;
import com.chalenge.wallet.dtos.WalletDTO;
import com.chalenge.wallet.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@ExposesResourceFor(User.class)
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> create(@RequestParam(required = true) String name) throws Exception {
        UserDTO userDTO = modelMapper.map(userService.createUser(name), UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }
}
