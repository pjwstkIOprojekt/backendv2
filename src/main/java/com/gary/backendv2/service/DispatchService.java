package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.EmployeeShift;
import com.gary.backendv2.model.Dispatcher;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.repository.EmployeeShiftRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private final UserRepository userRepository;
    private final EmployeeService employeeService;

    public void startShift() {
        if (SecurityContextHolder.getContext() == null) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getUsername();

        User user = userRepository.getByEmail(email);
        if (user instanceof Dispatcher d) {
            EmployeeShift newShift = employeeService.startShift(d);

            List<EmployeeShift> employeeShifts = d.getShifts();
            employeeShifts.add(newShift);

            d.setShifts(employeeShifts);

            userRepository.save(d);

        } else throw new HttpException(HttpStatus.FORBIDDEN);
    }

}
