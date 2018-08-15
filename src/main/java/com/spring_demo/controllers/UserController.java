package com.spring_demo.controllers;

import com.spring_demo.models.Role;
import com.spring_demo.models.User;
import com.spring_demo.repositories.RoleRepository;
import com.spring_demo.repositories.UserRepository;
import com.spring_demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    public ModelAndView login(Model model){
        model.addAttribute("message","Invalid username or password.");
        ModelAndView mav=new ModelAndView();
        mav.addObject(model);
        mav.setViewName("users/login");
        return mav;
    }

    @GetMapping("/registration")
    public ModelAndView registration(){

        ModelAndView mav=new ModelAndView();
        mav.setViewName("/users/registration");
        return mav;
    }

    @PostMapping("/registration")
    public ModelAndView create(@RequestParam(name = "first_name", required = true) String firstName,
                               @RequestParam(name = "last_name", required = true) String lastName,
                               @RequestParam(name = "username", required = true) String username,
                               @RequestParam(name = "email", required = true) String email,
                               @RequestParam(name = "password", required = true) String password
                               ){
        User user=new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(getbCryptPasswordEncoder().encode(password));
        Role role=roleRepository.findByRoleId(3).get();
        Set<Role> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setActive(0);
        userRepository.save(user);

        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("no-reply@example.com");
        mailMessage.setSubject("Best Blog");
        mailMessage.setText(String.format(Locale.US,"Hello %s,\r\nThank you very much for registering in best blog.",user.getDisplayName()));
        emailService.sendEmail(mailMessage);

        ModelAndView mav=new ModelAndView();
        mav.setViewName("redirect:/login");
        return mav;
    }

    @GetMapping("/users")
    public ModelAndView index(){

        ModelAndView mav=new ModelAndView();
        mav.setViewName("users/index");
        return mav;
    }

    private BCryptPasswordEncoder getbCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
