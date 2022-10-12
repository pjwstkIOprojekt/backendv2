package com.gary.backendv2.repository;

import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.enums.AllergyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Integer> {
	Optional<Allergy> findByAllergyId(Integer id);
	boolean existsByAllergyType(AllergyType allergyType);
	boolean existsByAllergyName(String allergyName);
	boolean existsByOther(String other);
	List<Allergy> findAllByAllergyName(String allergyName);
}

