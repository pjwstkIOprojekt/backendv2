package com.gary.backendv2.repository;

import com.gary.backendv2.model.EventReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventReportRepository extends JpaRepository<EventReport, Integer> {
}
