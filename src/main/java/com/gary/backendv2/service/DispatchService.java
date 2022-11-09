package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.EmployeeShift;
import com.gary.backendv2.model.Dispatcher;
import com.gary.backendv2.model.MappedSchedule;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.repository.DispatchShiftRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private final UserRepository userRepository;
    private final DispatchShiftRepository dispatchShiftRepository;

    public void startShift() {
        if (SecurityContextHolder.getContext() == null) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getUsername();

        User user = userRepository.getByEmail(email);
        if (user instanceof Dispatcher d) {
            MappedSchedule workSchedule = d.getWorkSchedule().getMappedSchedule();

            LocalDateTime now = LocalDateTime.now();
            LocalTime currentTime = now.toLocalTime();

            List<EmployeeShift> shifts = d.getShifts();

            EmployeeShift newShift = new EmployeeShift();
            newShift.setStartTime(now);
            newShift.setEndTime(currentTime.plusHours(8).atDate(LocalDate.now()));
            var delta = workSchedule.getWorkSchedule().get(LocalDate.now().getDayOfWeek()).getLeft().until(currentTime, ChronoUnit.MINUTES);
            newShift.setStartTimeDelta(delta);
            newShift.setActualEndTime(newShift.getEndTime().plusMinutes(delta));
            newShift.setEmployee(d);

            newShift = dispatchShiftRepository.save(newShift);
            shifts.add(newShift);

            d.setShifts(shifts);

            userRepository.save(d);

        } else throw new HttpException(HttpStatus.FORBIDDEN);
    }

}
