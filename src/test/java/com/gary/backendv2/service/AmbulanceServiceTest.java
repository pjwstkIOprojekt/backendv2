package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.exception.NotFoundException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Ambulance;

import com.gary.backendv2.model.AmbulanceHistory;
import com.gary.backendv2.model.AmbulanceState;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.response.AmbulanceResponse;
import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.AmbulanceType;
import com.gary.backendv2.repository.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmbulanceServiceTest {
    private final ItemService itemService = mock(ItemService.class);
    private final AmbulanceRepository ambulanceRepository = mock(AmbulanceRepository.class);
    private final AmbulanceStateRepository ambulanceStateRepository = mock(AmbulanceStateRepository.class);
    private final AmbulanceHistoryRepository ambulanceHistoryRepository = mock(AmbulanceHistoryRepository.class);
    private final AmbulanceLocationRepository ambulanceLocationRepository = mock(AmbulanceLocationRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final ItemContainerRepository itemContainerRepository = mock(ItemContainerRepository.class);
    private final InventoryRepository inventoryRepository = mock(InventoryRepository.class);
    private final AmbulanceService ambulanceService = new AmbulanceService(
            itemService,
            ambulanceRepository,
            ambulanceStateRepository,
            ambulanceHistoryRepository,
            ambulanceLocationRepository,
            itemRepository,
            itemContainerRepository,
            inventoryRepository);


    @Test
    void getAllAmbulances() {
        List<Ambulance> expected = List.of(new Ambulance());

        when(ambulanceRepository.findAll()).thenReturn(expected);


        var result = ambulanceRepository.findAll();

        assertEquals(expected.size(), result.size());
    }

    @Test
    void getAmbulanceByLicensePlateShouldFind() {

        String licensePlate = "WPI208";
        Ambulance expected = new Ambulance();
        expected.setLicensePlate(licensePlate);
        AmbulanceHistory ah = new AmbulanceHistory();
        AmbulanceState st = new AmbulanceState();
        st.setStateId(1);
        st.setStateType(AmbulanceStateType.AVAILABLE);
        st.setTimestamp(LocalDateTime.now());
        ah.setAmbulanceStates(List.of(st));
        expected.setAmbulanceHistory(ah);

        when(ambulanceRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.of(expected));

        AmbulanceResponse result = ambulanceService.getAmbulanceByLicensePlate("WPI208");

        assertNotNull(result);
        assertEquals(expected.getLicensePlate(), result.getLicensePlate());
    }

    @Test
    void getAmbulanceByLicensePlateShouldNotFind() {
        String licensePlate = "siema";


        when(ambulanceRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.empty());

        Exception exc = assertThrows(HttpException.class,
                () -> {
                    ambulanceService.getAmbulanceByLicensePlate(licensePlate);
                });

        String expectedMess = String.format("404 Ambulance %s not found", licensePlate);
        String actualMess = exc.getMessage();

        assertTrue(actualMess.contains(expectedMess));
    }


    @Test
    void addAmbulance() {
        AddAmbulanceRequest ambulanceRequest = new AddAmbulanceRequest();
        ambulanceRequest.setAmbulanceType(AmbulanceType.A);
        ambulanceRequest.setLicensePlate("WPI208");
        ambulanceRequest.setAmbulanceClass(AmbulanceClass.BASIC);
        ambulanceRequest.setSeats(3);
        ambulanceRequest.setLongitude(2.0);
        ambulanceRequest.setLatitude(3.0);

        Ambulance expected = new Ambulance();
        AmbulanceHistory ah = new AmbulanceHistory();
        AmbulanceState st = new AmbulanceState();
        st.setStateId(1);
        st.setStateType(AmbulanceStateType.AVAILABLE);
        st.setTimestamp(LocalDateTime.now());
        ah.setAmbulanceStates(List.of(st));
        expected.setAmbulanceHistory(ah);

        when(ambulanceRepository.save(any(Ambulance.class))).thenReturn(expected);

        ambulanceService.addAmbulance(ambulanceRequest);

        verify(ambulanceRepository, times(1)).save(any(Ambulance.class));
    }


    @Test
    void updateAmbulance() {
        AddAmbulanceRequest ambulanceRequest = new AddAmbulanceRequest();
        ambulanceRequest.setAmbulanceType(AmbulanceType.A);
        ambulanceRequest.setLicensePlate("WPI208");
        ambulanceRequest.setAmbulanceClass(AmbulanceClass.BASIC);
        ambulanceRequest.setSeats(3);
        ambulanceRequest.setLongitude(2.0);
        ambulanceRequest.setLatitude(3.0);

        when(ambulanceRepository.findByLicensePlate("WPI208")).thenReturn(Optional.ofNullable(new Ambulance()));

        ambulanceService.updateAmbulance(ambulanceRequest);

        verify(ambulanceRepository, times(1)).save(any(Ambulance.class));
    }

    @Test
    void updateAmbulanceShouldFail() {
        String plates = "WPI208";

        AddAmbulanceRequest ambulanceRequest = new AddAmbulanceRequest();
        ambulanceRequest.setAmbulanceType(AmbulanceType.A);
        ambulanceRequest.setLicensePlate(plates);
        ambulanceRequest.setAmbulanceClass(AmbulanceClass.BASIC);
        ambulanceRequest.setSeats(3);
        ambulanceRequest.setLongitude(2.0);
        ambulanceRequest.setLatitude(3.0);

        Exception exc = assertThrows(HttpException.class, () -> {
            ambulanceService.updateAmbulance(ambulanceRequest);
        });

        String expected = String.format("404 Ambulance %s not found", plates);
        String actual = exc.getMessage();

        assertEquals(expected, actual);
    }
}



