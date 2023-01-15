package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Backup;
import com.gary.backendv2.model.dto.request.BackupAddRequest;
import com.gary.backendv2.model.dto.request.BackupUpdateRequest;
import com.gary.backendv2.model.dto.response.BackupResponse;
import com.gary.backendv2.model.enums.BackupType;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.security.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BackupServiceTest {
    private final  BackupRepository backupRepository = mock(BackupRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final IncidentRepository incidentRepository = mock(IncidentRepository.class);
    private final BackupService backupService = new BackupService(backupRepository, userRepository, incidentRepository);


    @Test
    public void testAddWithValidInput() {
        BackupAddRequest backupAddRequest = new BackupAddRequest();
        backupAddRequest.setRequester("test@test.com");
        backupAddRequest.setIncidentId(1);
        backupAddRequest.setAccepted(true);
        backupAddRequest.setBackupType(BackupType.AMBULANCE);
        backupAddRequest.setJustification("Test justification");

        User user = new User();
        user.setEmail("test@test.com");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Incident incident = new Incident();
        incident.setIncidentId(1);
        doReturn(Optional.of(incident)).when(incidentRepository).findByIncidentId(any());

        backupService.add(backupAddRequest);
        verify(backupRepository, times(1)).save(any(Backup.class));
    }

    @Test
    public void testGetByIdWithValidInput() {
        Backup backup = new Backup();
        backup.setBackupId(1);
        backup.setBackupType(BackupType.AMBULANCE);
        backup.setAccepted(true);
        backup.setJustification("Test justification");
        backup.setTime(LocalDateTime.now());
        doReturn(Optional.of(backup)).when(backupRepository).findByBackupId(anyInt());

        BackupResponse backupResponse = backupService.getById(1);


        verify(backupRepository, times(1)).findByBackupId(anyInt());
    }

    @Test
    public void testGetByIdWithInvalidInput() {
        doReturn(Optional.empty()).when(backupRepository).findByBackupId(anyInt());

        assertThrows(HttpException.class, () -> backupService.getById(1));

        verify(backupRepository, times(1)).findByBackupId(anyInt());
    }

    @Test
    public void getAll() {

        Backup backup = new Backup();
        List<Backup> backups = List.of(backup);

        when(backupRepository.findAll()).thenReturn(backups);

        var result = backupService.getAll();

        assertEquals(backups.size(), result.size());

    }

    @Test
    public void update() {
        Backup backup = new Backup();
        BackupUpdateRequest request = new BackupUpdateRequest();
        when(backupRepository.findByBackupId(1)).thenReturn(Optional.of(backup));

        backupService.update(1, request);


        verify(backupRepository).save(backup);
    }

    @Test
    public void delete() {
        Backup backup = new Backup();
        when(backupRepository.findByBackupId(1)).thenReturn(Optional.of(backup));

        backupService.delete(1);

        verify(backupRepository).delete(backup);
    }

}
