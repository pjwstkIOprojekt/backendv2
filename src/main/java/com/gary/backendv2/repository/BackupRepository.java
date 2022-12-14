package com.gary.backendv2.repository;

import com.gary.backendv2.model.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupRepository extends JpaRepository<Backup, Integer> {
}
