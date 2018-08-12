package com.spring_demo.controllers;

import com.spring_demo.models.Role;
import com.spring_demo.models.User;
import com.spring_demo.repositories.RoleRepository;
import com.spring_demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.util.*;

@RestController
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/admins")
    public ModelAndView index(Model model){

        List<User> users=userRepository.findAll();
        model.addAttribute("users",users);
        ModelAndView mav=new ModelAndView();
        mav.setViewName("admins/index");
        return mav;
    }

    @GetMapping("/admins/user/{id}/edit")
    public ModelAndView userEdit(Model model, @PathVariable("id") int id){
        User user=userRepository.findByUserId(id).get();
        List<Role> roles=roleRepository.findAll();
        Collections.sort(roles, new Comparator<Role>() {
            @Override
            public int compare(Role role1, Role role2) {
                return role2.getRoleId()-role1.getRoleId();
            }
        });
        model.addAttribute("user",user);
        model.addAttribute("roles",roles);
        ModelAndView mav=new ModelAndView();
        mav.setViewName("admins/user_edit");
        return mav;
    }

    @PatchMapping("/admins/user/{id}/update")
    public ModelAndView userUpdate(Model model, @PathVariable("id") int id,
                                    @RequestParam(value = "first_name", required = true) String fistName,
                                    @RequestParam(value = "last_name", required = true) String lastName,
                                    @RequestParam(value = "role", required = true) int roleId,
                                    @RequestParam(value = "active", required = true) int active
                                   ){
        User user=userRepository.findByUserId(id).get();
        user.setFirstName(fistName);
        user.setLastName(lastName);
        user.setActive(active);
        Role role=roleRepository.findByRoleId(roleId).get();
        Set<Role> roles=new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        ModelAndView mav=new ModelAndView();
        mav.setViewName("redirect:/admins/user/"+id+"/edit");
        return mav;
    }

}
