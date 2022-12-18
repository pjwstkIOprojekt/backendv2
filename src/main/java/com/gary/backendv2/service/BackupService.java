package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Backup;
import com.gary.backendv2.model.dto.request.BackupUpdateRequest;
import com.gary.backendv2.model.dto.response.BackupResponse;
import com.gary.backendv2.repository.BackupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

	public List<BackupResponse> getAll(){
		List<BackupResponse> backupResponses = new ArrayList<>();
		for(Backup backup:backupRepository.findAll()){
			backupResponses.add(
					BackupResponse
							.builder()
							.time(backup.getTime())
							.justification(backup.getJustification())
							.accepted(backup.getAccepted())
							.backupType(backup.getBackupType())
							.backupId(backup.getBackupId())
							.build()
			);
		}
		return backupResponses;
	}

	public void update(Integer id, BackupUpdateRequest backupUpdateRequest){
		Optional<Backup> backupOptional = backupRepository.findByBackupId(id);
		if (backupOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find backup with %s", id));
		}
		Backup backup = backupOptional.get();
		backup.setBackupType(backupUpdateRequest.getBackupType());
		backup.setAccepted(backupUpdateRequest.getAccepted());
		backup.setJustification(backup.getJustification());
		backupRepository.save(backup);
	}

	public void delete(Integer id){
		Optional<Backup> backupOptional = backupRepository.findByBackupId(id);
		if (backupOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find backup with %s", id));
		}
		backupRepository.delete(backupOptional.get());
	}
}
