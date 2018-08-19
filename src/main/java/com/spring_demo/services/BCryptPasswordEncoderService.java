package com.spring_demo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordEncoderService {
    private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
    public String encode(String plainText){
        return bCryptPasswordEncoder.encode(plainText);
    }

    public boolean matches(String rawPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword,encodedPassword);
    }

    public BCryptPasswordEncoder getEncoder(){
        return bCryptPasswordEncoder;
    }
}
