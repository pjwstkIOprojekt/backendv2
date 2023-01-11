package com.gary.backendv2.model.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    Optional<ResetPasswordToken> findByTokenOrderByCreatedAtDesc(String token);
}