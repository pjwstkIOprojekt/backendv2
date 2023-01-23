package com.gary.backendv2.repository;

import com.gary.backendv2.model.ambulance.AmbulanceLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AmbulanceLocationRepository extends JpaRepository<AmbulanceLocation, Integer> {
    List<AmbulanceLocation> getAmbulanceLocationByAmbulance_LicensePlate(String licensePlate);

    @Query(
            nativeQuery = true,
            value = "select al.* from ambulance_location al\n" +
                    "where al.ambulance_id = ?1 and al.incident = ?2"
    )
    List<AmbulanceLocation> getAmbulanceLocationHistoryInIncident(Integer ambulanceId, Integer incidentId);


}