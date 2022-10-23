package com.gary.backendv2.repository;

import com.gary.backendv2.model.TrustedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrustedPersonRepository extends JpaRepository<TrustedPerson, Integer> {
}
