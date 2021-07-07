package com.inminhouse.alone.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inminhouse.alone.auth.config.security.TokenProvider;

@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenProvider tokenProvider;
    
    
//    public ResponseEntity<String> 
}
