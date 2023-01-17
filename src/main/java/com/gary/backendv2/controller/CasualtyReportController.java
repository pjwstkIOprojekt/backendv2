package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.CasualtyReportRequest;
import com.gary.backendv2.service.CasualtyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/casualty_controller")
@RequiredArgsConstructor
public class CasualtyReportController {

    private final CasualtyReportService casualtyReportService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(casualtyReportService.getAllCasualtyReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCasualtyReportById(@PathVariable Integer id) {
        return ResponseEntity.ok(casualtyReportService.getCasualtyReportById(id));
    }

    @PostMapping("/{id}")
    public void addDescription(@PathVariable Integer id, @RequestBody CasualtyReportRequest casualtyReportRequest) {
        casualtyReportService.addDiscription(id, casualtyReportRequest.getDescription());
    }

    @PostMapping("/{id}/{facilityId}")
    public void addFacility(@PathVariable Integer id, @PathVariable Integer facilityId) {
        casualtyReportService.addFacility(id, facilityId);
    }

    @PutMapping("/{id}")
    public void updateDescription(@PathVariable Integer id, @RequestBody CasualtyReportRequest casualtyReportRequest) {
        casualtyReportService.updateDescription(id, casualtyReportRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCasualtyReport(@PathVariable Integer id) {
        casualtyReportService.deleteCasualtyReport(id);
    }
}
