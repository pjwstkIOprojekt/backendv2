package com.gary.backendv2.repository;

import com.gary.backendv2.model.EquipmentInAmbulance;
import com.gary.backendv2.model.EquipmentInAmbulanceKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentInAmbulanceRepository extends JpaRepository<EquipmentInAmbulance, EquipmentInAmbulanceKey> {
}
