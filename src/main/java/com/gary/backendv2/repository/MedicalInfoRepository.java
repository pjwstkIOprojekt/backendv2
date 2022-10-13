package com.gary.backendv2.repository;

import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.enums.BloodType;
import com.gary.backendv2.model.enums.RhType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalInfoRepository extends JpaRepository<MedicalInfo, Integer> {
	MedicalInfo findByMedicalInfoId(Integer id);

	MedicalInfo findByRhTypeAndBloodType(RhType rhType, BloodType bloodType);
}
