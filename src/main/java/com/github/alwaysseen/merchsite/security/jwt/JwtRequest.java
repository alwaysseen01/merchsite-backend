package com.github.alwaysseen.merchsite.security.jwt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class JwtRequest {
    private String email;
    private String password;
}
