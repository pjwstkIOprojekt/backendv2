package com.gary.backendv2.repository;

import com.gary.backendv2.model.users.employees.Medic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicRepository extends JpaRepository<Medic, Integer> {
    List<Medic> getAllByUserIdIn(List<Integer> ids);
}