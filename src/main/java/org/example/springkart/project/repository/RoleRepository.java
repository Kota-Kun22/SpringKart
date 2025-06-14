package org.example.springkart.project.repository;

import org.example.springkart.project.model.AppRole;
import org.example.springkart.project.model.Role;
import org.example.springkart.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleRepository  extends  JpaRepository<Role,Long>{


    Optional<Role> findByRoleName(AppRole appRole);
}
