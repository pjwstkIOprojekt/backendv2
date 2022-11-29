package com.gary.backendv2.repository;

import com.gary.backendv2.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {
	Optional<Facility> findByFacilityId(Integer id);
}
