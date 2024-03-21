package com.github.alwaysseen.merchsite.services;

import com.github.alwaysseen.merchsite.entities.AppUser;
import com.github.alwaysseen.merchsite.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final AppUserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Optional<AppUser> getByEmail(String email){
        return Optional.ofNullable(userRepository.getByEmail(email));
    }

    public AppUser saveUser(AppUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
