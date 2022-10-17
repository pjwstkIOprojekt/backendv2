package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.exception.NotFoundException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.AllergyRequest;
import com.gary.backendv2.model.dto.response.AllergyResponse;
import com.gary.backendv2.model.enums.AllergyType;
import com.gary.backendv2.repository.AllergyRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AllergyServiceTest {
    private final AllergyRepository allergyRepository = mock(AllergyRepository.class);
    private final MedicalInfoRepository medicalInfoRepository = mock(MedicalInfoRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AllergyService allergyService = new AllergyService(allergyRepository, medicalInfoRepository, userRepository);

    @Test
    void getAll() {
        List<Allergy> expected = List.of(new Allergy());

        when(allergyRepository.findAll()).thenReturn(expected);

        var result = allergyService.getAll();

        assertEquals(expected.size(), result.size());
    }

    @Test()
    void getByIdShouldFind() {
       int id = 1;
       Allergy expected = new Allergy();
       expected.setAllergyId(1);
       expected.setAllergyName("test");

       when(allergyRepository.findByAllergyId(id)).thenReturn(Optional.of(expected));

       AllergyResponse result = allergyService.getById(1);

       assertNotNull(result);
       assertEquals(expected.getAllergyId(), result.getAllergyId());
       assertEquals(expected.getAllergyName(), result.getAllergyName());

    }

    @Test
    void getByIdShouldNotFind() {
        int id = 120;

        when(allergyRepository.findByAllergyId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(HttpException.class, () -> {
            allergyService.getById(id);
        });

        String expectedMessage = String.format("Cannot find allergy with %s", id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addAllergyNoMedialInfoShouldCreateAllergy() {
        AllergyRequest allergyRequest = new AllergyRequest();
        allergyRequest.setUserEmail("test@test.pl");
        allergyRequest.setAllergyName("test");
        allergyRequest.setAllergyType(AllergyType.INJECTION);

        User user = new User();
        MedicalInfo mi = new MedicalInfo();
        mi.setAllergies(new HashSet<>());
        user.setMedicalInfo(mi);

        when(userRepository.findByEmail(allergyRequest.getUserEmail())).thenReturn(Optional.of(user));
        when(allergyRepository.existsByAllergyName(allergyRequest.getAllergyName())).thenReturn(false);
        when(allergyRepository.existsByAllergyType(allergyRequest.getAllergyType())).thenReturn(false);
        when(allergyRepository.existsByOther(allergyRequest.getOther())).thenReturn(false);

        allergyService.addAllergy(allergyRequest);

        verify(allergyRepository, times(1)).save(any(Allergy.class));
        verify(medicalInfoRepository, times(1)).save(any(MedicalInfo.class));
    }

    @Test
    void addAllergyAllergyAndMedicalInfoFound() {
        AllergyRequest allergyRequest = new AllergyRequest();
        allergyRequest.setUserEmail("test@test.pl");
        allergyRequest.setAllergyName("test");
        allergyRequest.setAllergyType(AllergyType.INJECTION);

        MedicalInfo medicalInfo = new MedicalInfo();
        User user = new User();
        user.setMedicalInfo(medicalInfo);
        medicalInfo.setAllergies(new HashSet<>());
        Allergy allergy = new Allergy();
        allergy.setMedicalInfos(new HashSet<>());

        when(userRepository.findByEmail(allergyRequest.getUserEmail())).thenReturn(Optional.of(user));
        when(allergyRepository.existsByAllergyName(allergyRequest.getAllergyName())).thenReturn(true);
        when(allergyRepository.existsByAllergyType(allergyRequest.getAllergyType())).thenReturn(true);
        when(allergyRepository.existsByOther(allergyRequest.getOther())).thenReturn(true);
        when(allergyRepository.findByAllergyName(allergyRequest.getAllergyName())).thenReturn(Optional.of(allergy));
        when(allergyRepository.getByAllergyName(allergyRequest.getAllergyName())).thenReturn(allergy);

        allergyService.addAllergy(allergyRequest);

        verify(allergyRepository, times(1)).save(any(Allergy.class));
        verify(medicalInfoRepository, times(1)).save(any(MedicalInfo.class));
    }

    @Test
    void updateAllergyShouldUpdate() {
        Integer id = 1337;
        AllergyRequest allergyRequest = new AllergyRequest();
        allergyRequest.setUserEmail("test@test.pl");
        allergyRequest.setAllergyName("test");
        allergyRequest.setAllergyType(AllergyType.INJECTION);

        Allergy allergy = new Allergy();

        when(allergyRepository.findByAllergyId(id)).thenReturn(Optional.of(allergy));

        allergyService.updateAllergy(id, allergyRequest);

        verify(allergyRepository, times(1)).save(any(Allergy.class));
    }

    @Test
    void updateAllergyShouldFail() {
        Integer id = 1337;
        AllergyRequest allergyRequest = new AllergyRequest();
        allergyRequest.setUserEmail("test@test.pl");
        allergyRequest.setAllergyName("test");
        allergyRequest.setAllergyType(AllergyType.INJECTION);

        when(allergyRepository.findByAllergyId(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            allergyService.updateAllergy(id, allergyRequest);
        });

        String expected = "No value present";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void removeAllergyShouldDelete() {
        Integer id = 1337;
        Allergy allergy =new Allergy();

        when(allergyRepository.findByAllergyId(id)).thenReturn(Optional.of(allergy));

        allergyService.removeAllergy(id);

        verify(allergyRepository, times(1)).delete(any(Allergy.class));
    }

    @Test
    void removeAllergyShouldThrow() {
        Integer id = 1337;
        Allergy allergy =new Allergy();

        when(allergyRepository.findByAllergyId(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> {
            allergyService.removeAllergy(id);
        });

        String expected = "No record with that ID";
        String actual = exception.getMessage();

        verify(allergyRepository, times(0)).delete(any(Allergy.class));
        assertEquals(expected, actual);
    }
}