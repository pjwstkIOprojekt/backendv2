package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Backup;
import com.gary.backendv2.model.dto.request.BackupUpdateRequest;
import com.gary.backendv2.model.dto.response.BackupResponse;
import com.gary.backendv2.model.enums.BackupType;
import com.gary.backendv2.repository.BackupRepository;
import com.gary.backendv2.repository.IncidentRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BackupServiceTest {
	private final BackupRepository backupRepository = mock(BackupRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final IncidentRepository incidentRepository = mock(IncidentRepository.class);
	private final BackupService backupService = new BackupService(backupRepository, userRepository, incidentRepository);

	@Test
	void getAll(){
		List<Backup> expected = List.of(new Backup());
		when(backupRepository.findAll()).thenReturn(expected);
		var result = backupService.getAll();
		assertEquals(expected.size(), result.size());
	}

	@Test
	void getByIdShouldFind(){
		int id = 1;
		Backup expected = new Backup();
		expected.setBackupId(1);
		expected.setJustification("test");

		when(backupRepository.findByBackupId(id)).thenReturn(Optional.of(expected));

		BackupResponse result = backupService.getById(1);

		assertNotNull(result);
		assertEquals(expected.getBackupId(), result.getBackupId());
		assertEquals(expected.getJustification(), result.getJustification());
	}

	@Test
	void getByIdShoulNotFind(){
		int id = 2137;
		when(backupRepository.findByBackupId(id)).thenReturn(Optional.empty());

		Exception exception = assertThrows(HttpException.class, () -> {
			backupService.getById(id);
		});

		String expectedMessage = String.format("Cannot find backup with %s", id);
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void deleteShouldWork(){
		Integer id = 1337;
		Backup backup =new Backup();

		when(backupRepository.findByBackupId(id)).thenReturn(Optional.of(backup));

		backupService.delete(id);

		verify(backupRepository, times(1)).delete(any(Backup.class));
	}

	@Test
	void deleteShouldThrow(){
		Integer id = 1337;
		Backup backup =new Backup();

		when(backupRepository.findByBackupId(id)).thenReturn(Optional.empty());
		Exception exception = assertThrows(HttpException.class, () -> {
			backupService.delete(id);
		});

		String expected = new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find backup with %s", id)).getMessage();
		String actual = exception.getMessage();

		verify(backupRepository, times(0)).delete(any(Backup.class));
		assertEquals(expected, actual);
	}

	@Test
	void updateShouldWork(){
		Integer id = 1337;
		BackupUpdateRequest updateRequest = new BackupUpdateRequest();
		updateRequest.setBackupType(BackupType.AMBULANCE);

		Backup backup = new Backup();

		when(backupRepository.findByBackupId(id)).thenReturn(Optional.of(backup));

		backupService.update(id, updateRequest);

		verify(backupRepository, times(1)).save(any(Backup.class));
	}
}
