package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import com.gary.backendv2.model.dto.request.users.UpdateWorkScheduleRequest;
import com.gary.backendv2.model.dto.response.AmbulanceResponse;
import com.gary.backendv2.model.dto.response.WorkScheduleResponse;
import com.gary.backendv2.model.dto.response.users.GenericEmployeeResponse;
import com.gary.backendv2.model.dto.response.users.MedicResponse;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.model.users.employees.*;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.EmployeeShiftRepository;
import com.gary.backendv2.repository.MedicRepository;
import com.gary.backendv2.repository.UserRepository;
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeShiftRepository employeeShiftRepository;
    private final UserRepository userRepository;
    private final MedicRepository medicRepository;

    private final AmbulanceRepository ambulanceRepository;
    private final AuthService authService;

    public void startShift(Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);

        if (currentUser instanceof AbstractEmployee e) {
            List<EmployeeShift> allShifts = e.getShifts();
            if (allShifts.size() > 0) {
                EmployeeShift mostRecentShift = allShifts.get(allShifts.size() - 1);

                if (
                        (mostRecentShift.getActualStartTime() != null) &&
                        (mostRecentShift.getActualStartTime().getDayOfMonth() == LocalDate.now().getDayOfMonth()) &&
                        (mostRecentShift.getActualStartTime().getMonthValue() == LocalDate.now().getMonthValue()) &&
                        (mostRecentShift.getActualStartTime().getDayOfWeek() == LocalDate.now().getDayOfWeek())
                )
                    throw new HttpException(
                            HttpStatus.BAD_REQUEST,
                            String.format("You've already started a shift for: %s %s", LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue()));
            }

            EmployeeShift newShift = startNewShift(e);

            allShifts.add(newShift);
            e.setShifts(allShifts);

            userRepository.save(e);

        }
        else throw new HttpException(HttpStatus.FORBIDDEN, "Logged user seems not to be an Employee");
    }

    public void endShift(Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);
        if (currentUser instanceof AbstractEmployee e) {
            EmployeeShift currentShift = e.getCurrentShift();
            currentShift.setActualEndTime(LocalDateTime.now());

            employeeShiftRepository.save(currentShift);
        }
    }

    public WorkScheduleResponse updateWorkSchedule(UpdateWorkScheduleRequest workSchedule, Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);
        if (currentUser instanceof AbstractEmployee employee) {
            String json = Utils.POJOtoJsonString(workSchedule.getWorkSchedule());

            employee.getWorkSchedule().setSchedule(json);
            employee.getWorkSchedule().setCreatedAt(LocalDateTime.now());

            employee = userRepository.save(employee);

            return getWorkScheduleResponse(employee);
        }

        throw new HttpException(HttpStatus.I_AM_A_TEAPOT);
    }


    public WorkScheduleResponse getSchedule(Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);
        if (currentUser instanceof AbstractEmployee employee) {
            return getWorkScheduleResponse(employee);
        }

        throw new HttpException(HttpStatus.I_AM_A_TEAPOT);
    }

    public List<GenericEmployeeResponse> getAllSchedules() {
        List<GenericEmployeeResponse> responses = new ArrayList<>();

        List<AbstractEmployee> employees = userRepository.findAllEmployees();
        for (AbstractEmployee e : employees) {
            GenericEmployeeResponse response = new GenericEmployeeResponse();

            response.setEmail(e.getEmail());
            response.setId(e.getUserId());
            response.setName(e.getFirstName());
            response.setLastName(e.getLastName());
            response.setWorkSchedule(getWorkScheduleResponse(e));

            if (e instanceof Dispatcher d) {
                response.setEmployeeType(EmployeeType.DISPATCHER);
            }

            if (e instanceof Medic m) {
                response.setEmployeeType(EmployeeType.MEDIC);
            }

            responses.add(response);
        }

        return responses;
    }

    public AmbulanceResponse findAssignedAmbulance(Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);
        if (currentUser instanceof Medic m) {
            Optional<Ambulance> ambulanceOptional = ambulanceRepository.findAssignedAmbulance(m.getUserId());
            if (ambulanceOptional.isEmpty()) {
                throw new HttpException(HttpStatus.NOT_FOUND, String.format("Medic %s is not assigned to any ambulance", m.getUserId()));
            } else {
                return AmbulanceResponse.of(ambulanceOptional.get());
            }

        } else {
            throw new HttpException(HttpStatus.FORBIDDEN, "Not a medic");
        }
    }

    public List<MedicResponse> getAllMedics(){
        List<MedicResponse> medicResponses = new ArrayList<>();
        for(Medic m : medicRepository.findAll()){
            medicResponses.add(
                  MedicResponse
                          .builder()
                          .email(m.getEmail())
                          .firstName(m.getFirstName())
                          .lastName(m.getLastName())
                          .userId(m.getUserId())
                          .build()
            );
        }
        return medicResponses;
    }

    private WorkScheduleResponse getWorkScheduleResponse(AbstractEmployee employee) {
        WorkSchedule newSchedule = employee.getWorkSchedule();
        MappedSchedule mappedSchedule = newSchedule.getMappedSchedule();

        WorkScheduleResponse response = new WorkScheduleResponse();

        for (var kv : mappedSchedule.getTimeTable().entrySet()) {
            response.getSchedule().put(
                    String.valueOf(kv.getKey()),
                    new RegisterEmployeeRequest.ScheduleDto(
                            mappedSchedule.getWorkingHours(kv.getKey()).getLeft().toString(),
                            mappedSchedule.getWorkingHours(kv.getKey()).getRight().toString()));
        }
        return response;
    }

    private EmployeeShift startNewShift(AbstractEmployee e) {
        MappedSchedule workSchedule = e.getWorkSchedule().getMappedSchedule();
        Pair<LocalTime, LocalTime> todaysWorkingHours = workSchedule.getWorkingHours(LocalDate.now().getDayOfWeek());
        long shiftDuration = todaysWorkingHours.getLeft().until(todaysWorkingHours.getRight(), ChronoUnit.MINUTES);

        LocalTime currentTime = LocalTime.now();

        long delta = ChronoUnit.MINUTES.between(todaysWorkingHours.getLeft(), currentTime);

        EmployeeShift newShift = new EmployeeShift();
        newShift.setScheduledStartTime(LocalDateTime.of(LocalDate.now(), todaysWorkingHours.getLeft()));
        newShift.setScheduledEndTime(LocalDateTime.of(LocalDate.now(), todaysWorkingHours.getRight()));
        newShift.setActualStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        newShift.setExpectedEndTime(LocalDateTime.now().plusMinutes(shiftDuration));
        newShift.setStartTimeDelta(delta);
        newShift.setEmployee(e);

        return employeeShiftRepository.save(newShift);
    }

}
