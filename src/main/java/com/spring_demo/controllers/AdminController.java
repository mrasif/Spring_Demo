package com.spring_demo.controllers;

import com.spring_demo.models.User;
import com.spring_demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admins")
    public ModelAndView index(Model model){

        List<User> users=userRepository.findAll();
        model.addAttribute("users",users);
        ModelAndView mav=new ModelAndView();
        mav.setViewName("admins/index");
        return mav;
    }

    @GetMapping("/admins/user/{id}/edit")
    public ModelAndView userEdit(Model model){

        ModelAndView mav=new ModelAndView();
        mav.setViewName("admins/user_edit");
        return mav;
    }

}
