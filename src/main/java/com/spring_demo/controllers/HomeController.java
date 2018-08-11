package com.spring_demo.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    @GetMapping(value = "/")
    public ModelAndView index(Model model) {
        ModelAndView mav=new ModelAndView();
        mav.setViewName("homes/index");
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

    @GetMapping(value = "/users/example")
    public String example(){
        return "Example.";
    }

}
