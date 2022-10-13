package com.gary.backendv2.repository;

import com.gary.backendv2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getByEmail(String email);
    User getByUserId(Integer id);

    Boolean existsByEmail(String email);
}
