package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.DispatchShiftRequest;
import com.gary.backendv2.service.DispatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;

    @PostMapping("/start")
    public ResponseEntity<?> startShift(@RequestBody @Valid DispatchShiftRequest shiftRequest) {
        dispatchService.startShift(shiftRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("start_date", shiftRequest.getStart());
        response.put("end_date", shiftRequest.getHours() == null ? shiftRequest.getStart().plusHours(8) : shiftRequest.getStart().plusHours(shiftRequest.getHours()));

        return ResponseEntity.ok(response);
    }
}
