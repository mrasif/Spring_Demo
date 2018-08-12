package com.spring_demo.repositories;


import com.spring_demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.CookieStore;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(int userId);
}
