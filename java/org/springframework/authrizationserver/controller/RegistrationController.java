package org.springframework.authrizationserver.controller;

import org.springframework.authrizationserver.model.MyAppUser;
import org.springframework.authrizationserver.repository.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private MyAppUserRepository myAppUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup")
    public MyAppUser signup(@RequestBody MyAppUser myAppUser) {
        myAppUser.setPassword(passwordEncoder.encode(myAppUser.getPassword()));
        return myAppUserRepository.save(myAppUser);
    }
}
