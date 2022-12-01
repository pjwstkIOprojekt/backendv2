package com.gary.backendv2.repository;

import com.gary.backendv2.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial,Integer> {
}
