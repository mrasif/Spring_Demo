package com.spring_demo.services;

import com.spring_demo.models.User;
import com.spring_demo.models.UserDetailsImpl;
import com.spring_demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser=userRepository.findByUsername(username);

        optionalUser.orElseThrow(()->new UsernameNotFoundException("Username not found."));

        return optionalUser.map(UserDetailsImpl::new).get();
    }

}
