package com.gary.backendv2.repository;

import com.gary.backendv2.model.ambulance.AmbulanceLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmbulanceLocationRepository extends JpaRepository<AmbulanceLocation, Integer> {
    List<AmbulanceLocation> getAmbulanceLocationByAmbulance_LicensePlate(String licensePlate);
}