package com.gary.backendv2.repository;

import com.gary.backendv2.model.users.employees.EmployeeShift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeShiftRepository extends JpaRepository<EmployeeShift, Integer> {
}