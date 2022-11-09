package com.gary.backendv2.service;

import com.gary.backendv2.model.AbstractEmployee;
import com.gary.backendv2.model.EmployeeShift;
import com.gary.backendv2.model.MappedSchedule;
import com.gary.backendv2.repository.EmployeeShiftRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
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

    public EmployeeShift startShift(AbstractEmployee e) {
        MappedSchedule workSchedule = e.getWorkSchedule().getMappedSchedule();
        Pair<LocalTime, LocalTime> todaysWorkingHours = workSchedule.getWorkingHours(LocalDate.now().getDayOfWeek());
        long shiftDuration = todaysWorkingHours.getLeft().until(todaysWorkingHours.getRight(), ChronoUnit.MINUTES);
        System.out.println(shiftDuration);


        LocalTime currentTime = LocalTime.now();

        long delta = ChronoUnit.MINUTES.between(todaysWorkingHours.getLeft(), currentTime);
        System.out.println(delta);

        EmployeeShift newShift = new EmployeeShift();
        newShift.setScheduledStartTime(LocalDateTime.of(LocalDate.now(), todaysWorkingHours.getLeft()));
        newShift.setScheduledEndTime(LocalDateTime.of(LocalDate.now(), todaysWorkingHours.getRight()));
        newShift.setActualStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        newShift.setActualEndTime(LocalDateTime.now().plusMinutes(shiftDuration));
        newShift.setStartTimeDelta(delta);
        newShift.setEmployee(e);

        return employeeShiftRepository.save(newShift);
    }
}
