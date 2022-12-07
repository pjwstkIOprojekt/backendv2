package com.gary.backendv2.repository;

import com.gary.backendv2.model.users.employees.Dispatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatcherRepository extends JpaRepository<Dispatcher, Integer> {
}
