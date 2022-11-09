package com.gary.backendv2.repository;

import com.gary.backendv2.model.EmployeeShift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispatchShiftRepository extends JpaRepository<EmployeeShift, Integer> {
}