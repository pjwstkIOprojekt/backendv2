package com.gary.backendv2.repository;

import com.gary.backendv2.model.users.employees.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {
}