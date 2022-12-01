package com.gary.backendv2.repository;

import com.gary.backendv2.model.ambulance.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AmbulanceRepository extends JpaRepository<Ambulance, Integer> {
    Optional<Ambulance> findByLicensePlate(String licensePlate);
    List<Ambulance> getAmbulancesByLicensePlateIsIn(List<String> licensePlates);
}
