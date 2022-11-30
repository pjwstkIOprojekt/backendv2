package com.gary.backendv2.repository;

import com.gary.backendv2.model.Dispatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DispatcherRepository extends JpaRepository<Dispatcher, Integer> {
}
