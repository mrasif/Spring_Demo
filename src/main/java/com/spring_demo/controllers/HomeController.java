package com.spring_demo.controllers;

import com.spring_demo.models.Role;
import com.spring_demo.models.User;
import com.spring_demo.models.UserDetailsImpl;
import com.spring_demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.function.Function;

@RestController
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/")
    public ModelAndView index(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user=null;
//        try {
//            user = (User) auth.getPrincipal();
//        }
//        catch (Exception e){
//
//        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("homes/index");
        mav.setStatus(HttpStatus.OK);
        return mav;
    }
}
