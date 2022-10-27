package com.gary.backendv2.repository;

import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String prefixedName);
}