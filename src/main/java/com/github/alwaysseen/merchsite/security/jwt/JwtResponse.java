package com.github.alwaysseen.merchsite.security.jwt;

import com.github.alwaysseen.merchsite.entities.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private AppUser user;
}
