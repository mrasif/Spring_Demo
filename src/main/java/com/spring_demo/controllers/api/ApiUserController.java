package com.spring_demo.controllers.api;

import com.spring_demo.models.Role;
import com.spring_demo.models.User;
import com.spring_demo.models.UserDetailsImpl;
import com.spring_demo.repositories.RoleRepository;
import com.spring_demo.repositories.UserRepository;
import com.spring_demo.services.BCryptPasswordEncoderService;
import com.spring_demo.services.JWTService;
import com.spring_demo.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoderService bCryptPasswordEncoderService;

    @PostMapping("/auth/sign_in")
    public ResponseEntity signIn(@RequestParam String username, @RequestParam String password){
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);

        Map<String,Object> body=new HashMap<>();
        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        HttpStatus httpStatus=HttpStatus.OK;
        if (bCryptPasswordEncoderService.match(password,userDetails.getPassword())){
            User user=userRepository.findByUsername(userDetails.getUsername()).get();
            body.put("user",user);
            body.put("message","Login successfully!");
            String token=jwtService.createPayload(user);
            headers.add("access-tocken",token);
        }
        else {
            body.put("user",null);
            body.put("message","Invalid username or password!");
            httpStatus=HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body,headers,httpStatus);
    }

    @DeleteMapping("/auth/sign_out")
    public ResponseEntity signOut(@RequestHeader(value = "access-token",required = true) String token){
        Map<String,Object> body=new HashMap<>();
        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        HttpStatus httpStatus=HttpStatus.OK;

        if(jwtService.isAuthenticated(token)){
            body.put("user",jwtService.currentUser(token));
            body.put("message","Logout successfully!");
            jwtService.deletePayload(token);
        }
        else {
            body.put("message","Unauthorized!");
            httpStatus=HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body,headers,httpStatus);
    }

    @PostMapping("/auth/sign_up")
    public ResponseEntity signUp(@RequestBody User user){
        System.out.println("################################");
        System.out.println(user);
        System.out.println("################################");
        Map<String,Object> body=new HashMap<>();
        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        HttpStatus httpStatus=HttpStatus.OK;
        user.setPassword(bCryptPasswordEncoderService.encode(user.getConfirmPassword()));
        Role role=roleRepository.findByRoleId(3).get();
        Set<Role> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setActive(0);
        try {
            userRepository.save(user);
            body.put("user",userRepository.findByUsername(user.getUsername()));
            body.put("message","Register successfully!");
        }catch (Exception e){
            body.put("message","Registration failed!");
            httpStatus=HttpStatus.UNPROCESSABLE_ENTITY;
        }
        return new ResponseEntity<>(body,headers,httpStatus);
    }

    @GetMapping("/users")
    public ResponseEntity getUser(@RequestHeader(value = "access-token",required = true) String token){
        Map<String,Object> body=new HashMap<>();
        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        HttpStatus httpStatus=HttpStatus.OK;

        if(jwtService.isAuthenticated(token)){
            body.put("user",jwtService.currentUser(token));
            body.put("message","User fetched successfully!");
        }
        else {
            body.put("message","Unauthorized!");
            httpStatus=HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(body,headers,httpStatus);
    }

}
