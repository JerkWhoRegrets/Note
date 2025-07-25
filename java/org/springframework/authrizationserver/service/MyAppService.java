package org.springframework.authrizationserver.service;

import org.springframework.authrizationserver.model.MyAppUser;
import org.springframework.authrizationserver.repository.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyAppService implements UserDetailsService {
    @Autowired
    private MyAppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyAppUser> user = appUserRepository.findByUsername(username);
        if(user.isPresent()) {
            var userObj = user.get();
            return User.builder().username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
