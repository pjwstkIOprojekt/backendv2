package com.gary.backendv2.repository;

import com.gary.backendv2.model.Ambulance;
import com.gary.backendv2.model.TrustedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrustedPersonRepository extends JpaRepository<TrustedPerson, Integer> {
    Optional<TrustedPerson> findByEmail(String email);
}
