package com.gary.backendv2.repository;

import com.gary.backendv2.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Integer> {
	Disease findByDiseaseId(Integer id);
	boolean existsByDiseaseNameAndDescription(String diseaseName, String description);
	Disease findByDiseaseNameAndDescription(String diseaseName, String description);
}
