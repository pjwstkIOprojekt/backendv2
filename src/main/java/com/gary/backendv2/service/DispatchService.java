package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.DispatchShift;
import com.gary.backendv2.model.Dispatcher;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.DispatchShiftRequest;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.repository.DispatchShiftRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private final UserRepository userRepository;
    private final DispatchShiftRepository dispatchShiftRepository;

    public void startShift(DispatchShiftRequest startShiftRequest) {
        if (SecurityContextHolder.getContext() == null) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getUsername();

        User user = userRepository.getByEmail(email);
        if (user instanceof Dispatcher d) {
            List<DispatchShift> shifts = d.getShifts();
            DispatchShift newShift = new DispatchShift();
            newShift.setStartTime(startShiftRequest.getStart());
            newShift.setEndTime(startShiftRequest.getHours() == null ? newShift.getStartTime().plusHours(8) : newShift.getStartTime().plusHours(startShiftRequest.getHours()));
            newShift.setDispatcher(d);

            newShift = dispatchShiftRepository.save(newShift);
            shifts.add(newShift);

            d.setShifts(shifts);

            userRepository.save(d);

        } else throw new HttpException(HttpStatus.FORBIDDEN);
    }

}
