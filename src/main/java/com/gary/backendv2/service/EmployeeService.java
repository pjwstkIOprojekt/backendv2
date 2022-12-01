package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.users.employees.AbstractEmployee;
import com.gary.backendv2.model.users.employees.EmployeeShift;
import com.gary.backendv2.model.users.employees.MappedSchedule;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.repository.EmployeeShiftRepository;
import com.gary.backendv2.repository.UserRepository;
import com.gary.backendv2.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeShiftRepository employeeShiftRepository;
    private final UserRepository userRepository;

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
