package com.gary.backendv2.controller;

import com.gary.backendv2.model.AbstractEmployee;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.WorkSchedule;
import com.gary.backendv2.repository.UserRepository;
import com.gary.backendv2.repository.WorkScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dispatch")
public class DispatchController {
    private final WorkScheduleRepository workScheduleRepository;
    private final UserRepository userRepository;


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
