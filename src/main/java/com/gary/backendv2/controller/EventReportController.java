package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.EventReportRequest;
import com.gary.backendv2.service.EventReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/event_report")
@RequiredArgsConstructor
public class EventReportController {

    private final EventReportService eventReportService;

    @GetMapping
    public ResponseEntity<?> getAll() {return ResponseEntity.ok(eventReportService.getAllReportedEvents());}

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {return ResponseEntity.ok(eventReportService.getReportedEventById(id));}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        eventReportService.deleteEventReport(id);
        return ResponseEntity.ok("report deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody EventReportRequest eventReportRequest) {
        eventReportService.updateEventReport(id,eventReportRequest);
        return ResponseEntity.ok("updated succesfully");
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid EventReportRequest eventReportRequest) {
        eventReportService.addReportEvent(eventReportRequest);
        return ResponseEntity.ok("report added");
    }

}