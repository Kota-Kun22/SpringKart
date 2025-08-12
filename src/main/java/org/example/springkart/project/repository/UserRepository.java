package org.example.springkart.project.repository;

import org.example.springkart.project.model.AppRole;
import org.example.springkart.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u from User u Join u.roles r where r.roleName=:role")
    Page<User> findByRoleName(@Param("role") AppRole appRole, Pageable pageDetails);
}
