package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.users.UpdateWorkScheduleRequest;
import com.gary.backendv2.model.dto.response.AmbulanceResponse;
import com.gary.backendv2.model.dto.response.IncidentResponse;
import com.gary.backendv2.model.dto.response.WorkScheduleResponse;
import com.gary.backendv2.model.dto.response.users.DispatcherResponse;
import com.gary.backendv2.model.dto.response.users.GenericUserResponse;
import com.gary.backendv2.model.dto.response.users.MedicResponse;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.users.employees.*;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.repository.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeShiftRepository employeeShiftRepository;
    private final UserRepository userRepository;
    private final MedicRepository medicRepository;

    private final AmbulanceRepository ambulanceRepository;
    private final AuthService authService;
    private final IncidentRepository incidentRepository;
    private final DispatcherRepository dispatcherRepository;

    public void startShift(Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);

        if (currentUser instanceof AbstractEmployee e) {
            startShift(e);
        }
        else throw new HttpException(HttpStatus.FORBIDDEN, "Logged user seems not to be an Employee");
    }

    public void startShift(AbstractEmployee employee) {
        List<EmployeeShift> allShifts = employee.getShifts();
//        if (allShifts.size() > 0) {
//            EmployeeShift mostRecentShift = allShifts.get(allShifts.size() - 1);
//
//            if (
//                    (mostRecentShift.getActualStartTime() != null) &&
//                            (mostRecentShift.getActualStartTime().getDayOfMonth() == LocalDate.now().getDayOfMonth()) &&
//                            (mostRecentShift.getActualStartTime().getMonthValue() == LocalDate.now().getMonthValue()) &&
//                            (mostRecentShift.getActualStartTime().getDayOfWeek() == LocalDate.now().getDayOfWeek())
//            )
//                throw new HttpException(
//                        HttpStatus.BAD_REQUEST,
//                        String.format("You've already started a shift for: %s %s", LocalDate.now().getDayOfMonth(), LocalDate.now().getMonth().toString()));
//        }

        EmployeeShift newShift = startNewShift(employee);

        allShifts.add(newShift);
        employee.setShifts(allShifts);

        userRepository.save(employee);
    }

    public void endShift(Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);
        if (currentUser instanceof AbstractEmployee e) {
           endShift(e);
        }
    }

    public void endShift(AbstractEmployee employee) {
        EmployeeShift currentShift = employee.getCurrentShift();
        currentShift.setActualEndTime(LocalDateTime.now());

        employeeShiftRepository.save(currentShift);
    }

    public WorkScheduleResponse updateWorkSchedule(UpdateWorkScheduleRequest workSchedule, Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);
        if (currentUser instanceof AbstractEmployee employee) {
            String json = Utils.POJOtoJsonString(workSchedule.getWorkSchedule());

            employee.getWorkSchedule().setSchedule(json);
            employee.getWorkSchedule().setCreatedAt(LocalDateTime.now());

            employee = userRepository.save(employee);

            return Utils.createWorkScheduleResponse(employee);
        }

        throw new HttpException(HttpStatus.I_AM_A_TEAPOT);
    }


    public WorkScheduleResponse getSchedule(Authentication authentication) {
        User currentUser = authService.getLoggedUserFromAuthentication(authentication);
        if (currentUser instanceof AbstractEmployee employee) {
            return Utils.createWorkScheduleResponse(employee);
        }

        throw new HttpException(HttpStatus.I_AM_A_TEAPOT);
    }

    public Boolean amIWorking(Authentication authentication) {
        User user = authService.getLoggedUserFromAuthentication(authentication);
        if (user instanceof AbstractEmployee employee) {
            return employee.getWorking();
        }

        throw new HttpException(HttpStatus.I_AM_A_TEAPOT);
    }

    public List<GenericUserResponse> getAllSchedules() {
        List<GenericUserResponse> responses = new ArrayList<>();

        List<AbstractEmployee> employees = userRepository.findAllEmployees();
        for (AbstractEmployee e : employees) {
            GenericUserResponse response = new GenericUserResponse();

            response.setEmail(e.getEmail());
            response.setId(e.getUserId());
            response.setName(e.getFirstName());
            response.setLastName(e.getLastName());
            response.setWorkSchedule(Utils.createWorkScheduleResponse(e));

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

    public List<MedicResponse> getAllMedics() {
        List<MedicResponse> medicResponses = new ArrayList<>();
        for(Medic m : medicRepository.findAll()) {
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

    public List<DispatcherResponse> getAllDispatchers() {
        List<DispatcherResponse> responses = new ArrayList<>();
        for(Dispatcher d : dispatcherRepository.findAll()) {
            DispatcherResponse response = new DispatcherResponse();
            response.setFirstName(d.getFirstName());
            response.setLastName(d.getLastName());
            response.setUserId(d.getUserId());
            response.setEmail(d.getEmail());

            Set<IncidentResponse> incidentResponses = d.getIncidents().stream().map(IncidentResponse::new).collect(Collectors.toSet());

            response.getAssignedIncidents().addAll(incidentResponses);
        }

        return responses;
    }

    public List<MedicResponse> getFreeMedics() {
        List<Medic> allMedics = medicRepository.findAll();
        List<Ambulance> allAmbulances = ambulanceRepository.findAll();

        Set<Medic> assigendMedics = new HashSet<>();
        for (Ambulance ambulance : allAmbulances) {
           for (Medic assigend :  ambulance.getCrew().getMedics()) {
               if (allMedics.contains(assigend)) {
                   assigendMedics.add(assigend);
               }
           }
        }

        Set<Medic> freeMedics = new HashSet<>(allMedics);
        freeMedics.removeAll(assigendMedics);
        List<MedicResponse> medicResponses = new ArrayList<>();
        for (Medic m : freeMedics) {
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

    private EmployeeShift startNewShift(AbstractEmployee e) {
        MappedSchedule workSchedule = e.getWorkSchedule().getMappedSchedule();
        Pair<LocalTime, LocalTime> todaysWorkingHours = workSchedule.getWorkingHours(LocalDate.now().getDayOfWeek());
        if (todaysWorkingHours == null) {
            throw new HttpException(HttpStatus.NO_CONTENT, "Your work schedule doesn't have entry for " + LocalDate.now().getDayOfWeek());
        }

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
