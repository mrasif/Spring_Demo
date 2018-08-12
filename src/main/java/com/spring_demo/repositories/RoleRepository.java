package com.spring_demo.repositories;


import com.spring_demo.models.Role;
import com.spring_demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleId(int roleId);
}
