package com.spring_demo.services;

import com.spring_demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    @Autowired
    private BCryptPasswordEncoderService bCryptPasswordEncoderService;

    Map<String,User> tokens=new HashMap<>();

    public String createPayload(User user){
        String token=bCryptPasswordEncoderService.encode(user.getUsername()+Calendar.getInstance().getTimeInMillis());
        tokens.put(token,user);
        System.out.println(tokens);
        return token;
    }

    public boolean deletePayload(String token){
        User user=tokens.remove(token);
        if (null!=user){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isAuthenticated(String token){
        User user=tokens.get(token);
        if (null!=user){
            return true;
        }
        else {
            return false;
        }
    }

    public User currentUser(String token){
        return tokens.get(token);
    }

}
