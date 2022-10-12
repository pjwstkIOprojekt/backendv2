package com.gary.backendv2.repository;

import com.gary.backendv2.model.MedicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalInfoRepository extends JpaRepository<MedicalInfo, Integer> {
	MedicalInfo findByMedicalInfoId(Integer id);
}
