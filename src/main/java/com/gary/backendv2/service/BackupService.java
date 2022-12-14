package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Backup;
import com.gary.backendv2.model.dto.response.BackupResponse;
import com.gary.backendv2.repository.BackupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackupService {
	private BackupRepository backupRepository;

	public BackupResponse getById(Integer id){
		Optional<Backup> backupOptional = backupRepository.findByBackupId(id);
		if (backupOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find backup with %s", id));
		}
		Backup backup = backupOptional.get();
		return BackupResponse
				.builder()
				.backupId(backup.getBackupId())
				.backupType(backup.getBackupType())
				.accepted(backup.getAccepted())
				.justification(backup.getJustification())
				.time(backup.getTime())
				.build();
	}
}
