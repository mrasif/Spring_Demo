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

    Map<String,Payload> tokens=new HashMap<>();

    public String createPayload(User user){
        String token=bCryptPasswordEncoderService.encode(user.getUsername());
        Calendar calendar=Calendar.getInstance();
//        calendar.add(Calendar.MINUTE,1);
        calendar.add(Calendar.DATE,15);
        long expired=calendar.getTimeInMillis();
        Payload payload=new Payload(expired,user);
        tokens.put(token,payload);
        System.out.println(tokens);
        return token;
    }

    public boolean deletePayload(String token){
        Payload payload=tokens.remove(token);
        if (null!=payload){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isAuthenticated(String token){
        Payload payload=tokens.get(token);
        long current_time=Calendar.getInstance().getTimeInMillis();
        if (null!=payload){
            if (payload.getExpired()<current_time){
                return deletePayload(token);
            }
            return true;
        }
        else {
            return false;
        }
    }

    public long getExpired(String token){
        Payload payload=tokens.get(token);
        if (null!=payload.getUser()){
            return payload.getExpired();
        }
        else {
            return 0;
        }
    }

    public User currentUser(String token){
        Payload payload=tokens.get(token);
        if (null!=payload){
            return tokens.get(token).getUser();
        }
        else {
            return null;
        }
    }

    class Payload{
        private long expired;
        private User user;

        public Payload() {
        }

        public Payload(long expired, User user) {
            this.expired = expired;
            this.user=user;
        }

        public long getExpired() {
            return expired;
        }

        public void setExpired(long expired) {
            this.expired = expired;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

}
