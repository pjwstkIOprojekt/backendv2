package com.gary.backendv2.repository;

import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.users.employees.AbstractEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getByEmail(String email);
    User getByUserId(Integer id);
    Boolean existsByEmail(String email);

    @Query(
            value = "select u.* from users u\n" +
                    "where u.dtype != 'User'",
            nativeQuery = true)
    List<AbstractEmployee> findAllEmployees();
    Optional<User> findByEmail(String email);
}
