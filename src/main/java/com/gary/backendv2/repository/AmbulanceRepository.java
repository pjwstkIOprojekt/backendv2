package com.gary.backendv2.repository;

import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.users.employees.Medic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AmbulanceRepository extends JpaRepository<Ambulance, Integer> {
    Optional<Ambulance> findByLicensePlate(String licensePlate);
    List<Ambulance> getAmbulancesByLicensePlateIsIn(List<String> licensePlates);

    @Query(
            value = "select a.* from ambulance a\n" +
                    "join crew c on a.crew_crew_id = c.crew_id\n" +
                    "join crew_medics cm on c.crew_id = cm.crew_crew_id\n" +
                    "join users u on cm.medics_user_id = u.user_id\n" +
                    "where u.user_id = ?1",
            nativeQuery = true)
    Optional<Ambulance> findAssignedMedic(Integer medicId);
}
