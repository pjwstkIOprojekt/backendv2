package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Backup;
import com.gary.backendv2.model.dto.request.BackupAddRequest;
import com.gary.backendv2.model.dto.request.BackupUpdateRequest;
import com.gary.backendv2.model.dto.response.BackupResponse;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.users.employees.AbstractEmployee;
import com.gary.backendv2.repository.BackupRepository;
import com.gary.backendv2.repository.IncidentRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackupService {
	private final BackupRepository backupRepository;
	private final UserRepository userRepository;
	private final IncidentRepository incidentRepository;

	public void add(BackupAddRequest backupAddRequest){
		Optional<User> userOptional = userRepository.findByEmail(backupAddRequest.getRequester());
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", backupAddRequest.getRequester()));
		}
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(backupAddRequest.getIncidentId());
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", backupAddRequest.getIncidentId()));
		Backup backup = Backup
				.builder()
				.accepted(backupAddRequest.getAccepted())
				.backupType(backupAddRequest.getBackupType())
				.incident(accidentReportOptional.get())
				.justification(backupAddRequest.getJustification())
				.time(LocalDateTime.now())
				.requester((AbstractEmployee) userOptional.get())
				.build();
		backupRepository.save(backup);
	}

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
