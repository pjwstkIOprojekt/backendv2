package com.gary.backendv2.service;

import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.repository.AllergyRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void getById() {
       assertFalse(false);
    }

    @Test
    void addAllergy() {
    }

    @Test
    void updateAllergy() {
    }

    @Test
    void removeAllergy() {
    }
}