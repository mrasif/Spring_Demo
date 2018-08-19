package com.spring_demo.controllers.api;

import com.spring_demo.models.JSONResponse;
import com.spring_demo.models.Role;
import com.spring_demo.models.User;
import com.spring_demo.repositories.RoleRepository;
import com.spring_demo.repositories.UserRepository;
import com.spring_demo.services.BCryptPasswordEncoderService;
import com.spring_demo.services.JWTService;
import com.spring_demo.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity signIn(@RequestParam String username, @RequestParam String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        JSONResponse jsonResponse=new JSONResponse();

        if (null!=userDetails) {
            if (bCryptPasswordEncoderService.matches(password, userDetails.getPassword())) {
                User user = userRepository.findByUsername(userDetails.getUsername()).get();
                jsonResponse.addBody("user", user);
                jsonResponse.addBody("message", "Login successfully!");
                String token = jwtService.createPayload(user);
                jsonResponse.addHeader("access-tocken", token);
                Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(jwtService.getExpired(token));
                jsonResponse.addHeader("token-expiry",calendar.getTime().toString());
            } else {
                jsonResponse.addBody("user", null);
                jsonResponse.addBody("message", "Invalid username or password!");
                jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            jsonResponse.addBody("user", null);
            jsonResponse.addBody("message", "User is not activated by Administrator!");
            jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        return jsonResponse.toResponseEntity();
    }

    @DeleteMapping("/auth/sign_out")
    public ResponseEntity signOut(@RequestHeader(value = "access-token", required = true) String token) {
        JSONResponse jsonResponse=new JSONResponse();

        if (jwtService.isAuthenticated(token)) {
            jsonResponse.addBody("user", jwtService.currentUser(token));
            jsonResponse.addBody("message", "Logout successfully!");
            jwtService.deletePayload(token);
        } else {
            jsonResponse.addBody("message", "Unauthorized!");
            jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        return jsonResponse.toResponseEntity();
    }

    @PostMapping("/auth/sign_up")
    public ResponseEntity signUp(@RequestBody User user) {
        JSONResponse jsonResponse=new JSONResponse();
        user.setPassword(bCryptPasswordEncoderService.encode(user.getPassword()));
        Role role = roleRepository.findByRoleId(3).get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setActive(0);
        try {
            userRepository.save(user);
            jsonResponse.addBody("user", userRepository.findByUsername(user.getUsername()));
            jsonResponse.addBody("message", "Register successfully!");
        } catch (Exception e) {
            jsonResponse.addBody("message", "Registration failed!");
            jsonResponse.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return jsonResponse.toResponseEntity();
    }

    @GetMapping("/users")
    public ResponseEntity getUser(@RequestHeader(value = "access-token", required = true) String token) {
        JSONResponse jsonResponse=new JSONResponse();
        if (jwtService.isAuthenticated(token)) {
            User user=jwtService.currentUser(token);
            if (null==user){
                jsonResponse.addBody("message", "Unauthorized!");
                jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
            }
            else {
                jsonResponse.addBody("user", user);
                jsonResponse.addBody("message", "User fetched successfully!");
            }
        } else {
            jsonResponse.addBody("message", "Unauthorized!");
            jsonResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);
        }
        return jsonResponse.toResponseEntity();
    }

}
