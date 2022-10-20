package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Ambulance;
import com.gary.backendv2.model.AmbulanceHistory;
import com.gary.backendv2.model.AmbulanceState;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.UpdateAmbulanceStateRequest;
import com.gary.backendv2.model.dto.response.AmbulanceHistoryResponse;
import com.gary.backendv2.model.dto.response.AmbulanceResponse;
import com.gary.backendv2.model.dto.response.AmbulanceStateResponse;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.repository.AmbulanceHistoryRepository;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.AmbulanceStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmbulanceService {
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceStateRepository ambulanceStateRepository;
    private final AmbulanceHistoryRepository ambulanceHistoryRepository;

    public List<AmbulanceResponse> getAllAmbulances() {
        List<Ambulance> all = ambulanceRepository.findAll();
        List<AmbulanceResponse> ambulanceResponseList = new ArrayList<>();
        for (var ambulance : all) {
            ambulanceResponseList.add(AmbulanceResponse.of(ambulance));
        }

        return ambulanceResponseList;
    }

    public AmbulanceResponse getAmbulanceByLicensePlate(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));
        else return AmbulanceResponse.of(ambulanceOptional.get());
    }

    public AmbulanceHistoryResponse getAmbulanceHistory(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        AmbulanceHistoryResponse ambulanceHistoryResponse = new AmbulanceHistoryResponse();
        Ambulance ambulance = ambulanceOptional.get();
        Set<AmbulanceStateResponse> ambulanceStateResponseSet = populateStateResponseSet(ambulance);

        ambulanceHistoryResponse.setLicensePlate(ambulance.getLicensePlate());
        ambulanceHistoryResponse.setAmbulanceHistory(ambulanceStateResponseSet);

        return ambulanceHistoryResponse;
    }

    public AmbulanceStateResponse getAmbulanceCurrentState(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        Ambulance ambulance = ambulanceOptional.get();
        ambulance.findCurrentState();
        AmbulanceState currentState = ambulance.getCurrentState();

        AmbulanceStateResponse ambulanceStateResponse = new AmbulanceStateResponse();
        ambulanceStateResponse.setType(currentState.getStateType());
        ambulanceStateResponse.setTimeWindow(getStringLocalDateTimeMap(currentState));

        return ambulanceStateResponse;
    }

    public AmbulanceResponse addAmbulance(AddAmbulanceRequest addRequest) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(addRequest.getLicensePlate());
        if (ambulanceOptional.isPresent()) throw new HttpException(HttpStatus.CONFLICT, String.format("Ambulance %s already exists", addRequest.getLicensePlate()));

        Ambulance ambulance = ambulanceRepository.save(createAmbulance(addRequest));

        return AmbulanceResponse.of(ambulance);
    }

    public AmbulanceStateResponse changeAmbulanceState(String licensePlate, UpdateAmbulanceStateRequest newState) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        Ambulance ambulance = ambulanceOptional.get();
        AmbulanceHistory ambulanceHistory = ambulance.getAmbulanceHistory();

        AmbulanceState state = new AmbulanceState();
        state.setStateType(newState.getStateType());
        AmbulanceState.TimeWindow timeWindow = AmbulanceState.TimeWindow.fixed(newState.getStart(), newState.getEnd());
        state.setStateTimeWindow(timeWindow);

        state = ambulanceStateRepository.save(state);

        ambulance.setCurrentState(state);
        ambulanceRepository.save(ambulance);

        ambulanceHistory.getAmbulanceStates().add(state);
        ambulanceHistoryRepository.save(ambulanceHistory);

        AmbulanceStateResponse stateResponse = new AmbulanceStateResponse();
        stateResponse.setType(state.getStateType());
        stateResponse.setTimeWindow(getStringLocalDateTimeMap(state));
        return  stateResponse;
    }

    public void updateAmbulance(AddAmbulanceRequest addAmbulanceRequest) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(addAmbulanceRequest.getLicensePlate());
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", addAmbulanceRequest.getLicensePlate()));

        Ambulance ambulance = ambulanceOptional.get();

        ambulance.setLicensePlate(addAmbulanceRequest.getLicensePlate());
        ambulance.setAmbulanceType(addAmbulanceRequest.getAmbulanceType());
        ambulance.setAmbulanceClass(addAmbulanceRequest.getAmbulanceClass());
        ambulance.setSeats(addAmbulanceRequest.getSeats());

        ambulanceRepository.save(ambulance);
    }

    public void deleteAmbulance(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        ambulanceRepository.delete(ambulanceOptional.get());
    }

    private Set<AmbulanceStateResponse> populateStateResponseSet(Ambulance ambulance) {
        Set<AmbulanceStateResponse> responseSet = new HashSet<>();

        AmbulanceHistory ambulanceHistory = ambulance.getAmbulanceHistory();

        for (AmbulanceState state : ambulanceHistory.getAmbulanceStates()) {
            Map<String, LocalDateTime> timeMap = getStringLocalDateTimeMap(state);

            AmbulanceStateResponse response = new AmbulanceStateResponse();
            response.setType(state.getStateType());
            response.setTimeWindow(timeMap);

            responseSet.add(response);
        }

        return responseSet;
    }
    @Scheduled(fixedDelay = 60000 )
    public void checkForStateExpiration() {
        List<Ambulance> ambulances = ambulanceRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        for (Ambulance ambulance : ambulances) {
            AmbulanceState currentState = ambulance.findCurrentState();
            AmbulanceHistory history = ambulance.getAmbulanceHistory();

            if (currentState.getStateTimeWindow().getEndTime() != null && currentState.getStateTimeWindow().getEndTime().isBefore(now)) {
                AmbulanceState newState = new AmbulanceState();
                newState.setStateType(AmbulanceStateType.AVAILABLE);
                newState.setStateTimeWindow(AmbulanceState.TimeWindow.startingFrom(now));
                newState = ambulanceStateRepository.save(newState);

                history.getAmbulanceStates().add(newState);
                ambulanceHistoryRepository.save(history);
            }
        }
    }

    private Map<String, LocalDateTime> getStringLocalDateTimeMap(AmbulanceState state) {
        Map<String, LocalDateTime> timeMap = new HashMap<>();
        timeMap.put("start", state.getStateTimeWindow().getStartTime());
        // God, I hate languages that are not null safe...
        // it threw null pointer exception without this check
        if (state.getStateTimeWindow().getEndTime() == null) {
            timeMap.put("end", null);
        } else timeMap.put("end", state.getStateTimeWindow().getEndTime());
        return timeMap;
    }

    private Ambulance createAmbulance(AddAmbulanceRequest addRequest) {
        AmbulanceHistory ambulanceHistory = new AmbulanceHistory();

        AmbulanceState ambulanceState = new AmbulanceState();
        ambulanceState.setStateType(AmbulanceStateType.AVAILABLE);
        ambulanceState.setStateTimeWindow(AmbulanceState.TimeWindow.startingFrom(LocalDateTime.now()));
        ambulanceState = ambulanceStateRepository.save(ambulanceState);

        ambulanceHistory.getAmbulanceStates().add(ambulanceState);
        ambulanceHistory = ambulanceHistoryRepository.save(ambulanceHistory);

        Ambulance ambulance = new Ambulance();
        ambulance.setAmbulanceClass(addRequest.getAmbulanceClass());
        ambulance.setAmbulanceType(addRequest.getAmbulanceType());
        ambulance.setSeats(addRequest.getSeats());
        ambulance.setLicensePlate(addRequest.getLicensePlate());
        var locationAnyNull = Stream.of(addRequest.getLatitude(), addRequest.getLongitude()).anyMatch(Objects::isNull);
        ambulance.setLocation(locationAnyNull ? Ambulance.Location.undefined() : Ambulance.Location.of(addRequest.getLongitude(), addRequest.getLatitude()));
        ambulance.setCurrentState(ambulanceState);
        ambulance.setAmbulanceHistory(ambulanceHistory);

        return ambulance;
    }
}
