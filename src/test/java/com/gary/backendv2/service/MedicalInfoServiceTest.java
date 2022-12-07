package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.users.MedicalInfo;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.dto.request.BloodRequest;
import com.gary.backendv2.model.enums.BloodType;
import com.gary.backendv2.model.enums.RhType;
import com.gary.backendv2.repository.AllergyRepository;
import com.gary.backendv2.repository.DiseaseRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicalInfoServiceTest {

    private final MedicalInfoRepository medicalInfoRepository = mock(MedicalInfoRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AllergyRepository allergyRepository = mock(AllergyRepository.class);
    private final DiseaseRepository diseaseRepository = mock(DiseaseRepository.class);
    private final DiseaseService diseaseService = new DiseaseService(diseaseRepository, medicalInfoRepository, userRepository);
    private final AllergyService allergyService = new AllergyService(allergyRepository, medicalInfoRepository, userRepository);
    private final MedicalInfoService medicalInfoService = new MedicalInfoService(medicalInfoRepository, userRepository,allergyService, diseaseService);

    @Test
    void getBloodByMedicalShouldFind() {
        Integer id = 1337;
        MedicalInfo medicalInfo = new MedicalInfo();
        medicalInfo.setMedicalInfoId(33);
        User user = new User();
        user.setEmail("test@test.pl");
        medicalInfo.setBloodType(BloodType.A);
        medicalInfo.setRhType(RhType.MINUS);

        user.setMedicalInfo(medicalInfo);
        medicalInfo.setUser(user);

        when(medicalInfoRepository.findByMedicalInfoId(id)).thenReturn(Optional.of(medicalInfo));

        BloodRequest actual = medicalInfoService.getBloodByMedicalId(id);

        BloodRequest expected = new BloodRequest();
        expected.setBloodType(BloodType.A);
        expected.setRhType(RhType.MINUS);
        expected.setUserEmail("test@test.pl");

        assertEquals(expected.getBloodType(), actual.getBloodType());
        assertEquals(expected.getRhType(), actual.getRhType());
        assertEquals(expected.getUserEmail(), actual.getUserEmail());
    }

    @Test
    void getBloodByMedicalShouldFail() {
        Integer id = 1337;
        MedicalInfo medicalInfo = new MedicalInfo();
        medicalInfo.setMedicalInfoId(33);
        medicalInfo.setBloodType(BloodType.A);
        medicalInfo.setRhType(RhType.MINUS);

        when(medicalInfoRepository.findByMedicalInfoId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            medicalInfoService.getBloodByMedicalId(id);
        });

        String expected = String.format("404 Cannot find medical info with id %s", id);
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void addBloodMedicalInfo() {
        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setUserEmail("test@test.pl");
        bloodRequest.setBloodType(BloodType.B);
        bloodRequest.setRhType(RhType.MINUS);

        User user = new User();
        MedicalInfo mi = new MedicalInfo();
        mi.setAllergies(new HashSet<>());
        user.setEmail("test@test.pl");
        user.setMedicalInfo(mi);

        when(userRepository.findByEmail(bloodRequest.getUserEmail())).thenReturn(Optional.of(user));

        medicalInfoService.addBlood(bloodRequest);

        verify(medicalInfoRepository, times(1)).save(any(MedicalInfo.class));
    }


    @Test
    void removeBloodMedicalInfoFound() {
        Integer id = 1337;
        MedicalInfo medicalInfo = new MedicalInfo();

        when(medicalInfoRepository.findByMedicalInfoId(id)).thenReturn(Optional.of(medicalInfo));

        medicalInfoService.removeBlood(id);

        verify(medicalInfoRepository, times(1)).save(any(MedicalInfo.class));

    }

    @Test
    void removeBloodMedicalInfoNotFound() {
        Integer id = 1337;

        when(medicalInfoRepository.findByMedicalInfoId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            medicalInfoService.removeBlood(id);

        });

        String expected = String.format("404 Cannot find medical info with id %s", id);
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void updateBloodMedicalInfoFound() {
        Integer id = 1337;
        MedicalInfo medicalInfo = new MedicalInfo();

        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setRhType(RhType.MINUS);
        bloodRequest.setBloodType(BloodType.B);

        when(medicalInfoRepository.findByMedicalInfoId(id)).thenReturn(Optional.of(medicalInfo));

        medicalInfoService.updateBlood(id, bloodRequest);

        verify(medicalInfoRepository, times(1)).save(any(MedicalInfo.class));

    }

    @Test
    void updateBloodMedicalInfoNotFound() {
        Integer id = 1337;

        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setRhType(RhType.MINUS);
        bloodRequest.setBloodType(BloodType.B);

        when(medicalInfoRepository.findByMedicalInfoId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            medicalInfoService.updateBlood(id, bloodRequest);

        });

        String expected = String.format("404 Cannot find medical info with id %s", id);
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }
}