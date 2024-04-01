package com.github.alwaysseen.merchsite.security.jwt;

import com.github.alwaysseen.merchsite.entities.AppUser;
import com.github.alwaysseen.merchsite.entities.Role;
import com.github.alwaysseen.merchsite.services.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final AuthService authService;
    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder encoder;
    private final Logger l = LoggerFactory.getLogger(AuthController.class);

    record RegisterRequest(
            String fname,
            String lname,
            String email,
            String password,
            String role
    ){}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        try {
            final JwtResponse token = authService.login(authRequest, encoder);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam String token, @RequestParam TokenType tokenType) {
        boolean isValid = authService.checkTokenValidity(token, tokenType);
        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest user) throws Exception {
        Optional<AppUser> existingUser = userService.getByEmail(user.email());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
        }
        AppUser newUser = new AppUser();
        newUser.setFname(user.fname());
        newUser.setLname(user.lname());
        newUser.setEmail(user.email());
        newUser.setPassword(user.password());
        newUser.setRole(Role.valueOf(user.role()));
        newUser.setCreationDate(LocalDate.now());

        try {
            AppUser savedUser = userService.saveUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
