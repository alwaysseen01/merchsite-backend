package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.AppUser;
import com.github.alwaysseen.merchsite.entities.Role;
import com.github.alwaysseen.merchsite.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("api/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<AppUser>> getAll(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id/{user_id}")
    public ResponseEntity<AppUser> getById(@PathVariable("user_id") Integer id){
        AppUser user = userRepository.findById(id).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{user_id}")
    public ResponseEntity<AppUser> delete(@PathVariable("user_id") Integer id){
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
