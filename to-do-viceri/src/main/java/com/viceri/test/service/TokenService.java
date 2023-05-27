package com.viceri.test.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;


@Service
public class TokenService {

    private String tokenHolder;

    public String getToken() {
        return tokenHolder;
    }
    
    public void setToken(String token) {
       this.tokenHolder = token;
    }
    
    public static String extractUserFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }
}
