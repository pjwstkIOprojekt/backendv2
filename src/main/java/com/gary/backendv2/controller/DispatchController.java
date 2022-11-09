package com.gary.backendv2.controller;

import com.gary.backendv2.model.AbstractEmployee;
import com.gary.backendv2.model.MappedSchedule;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.WorkSchedule;
import com.gary.backendv2.repository.UserRepository;
import com.gary.backendv2.repository.WorkScheduleRepository;
import com.gary.backendv2.service.DispatchService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;
    private final WorkScheduleRepository workScheduleRepository;
    private final UserRepository userRepository;

    @GetMapping("/start")
    public ResponseEntity<?> startShift() {
        dispatchService.startShift();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/test")
    public void testJson() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:default-work-schedule.json");

        String json;
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            json = FileCopyUtils.copyToString(reader);
        }

        User employee = userRepository.findByEmail("dispatch@test.pl").orElseThrow();
        if (employee instanceof AbstractEmployee e) {
            WorkSchedule schedule = new WorkSchedule();
            schedule.setCreatedAt(LocalDateTime.now());
            schedule.setScheduleId(null);
            schedule.setSchedule(json);

            schedule = workScheduleRepository.save(schedule);

            e.setWorkSchedule(schedule);

            userRepository.save(e);
        }
    }
}
