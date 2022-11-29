package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.IncidentReportRequest;
import com.gary.backendv2.model.dto.request.AccidentReportUpdateRequest;
import com.gary.backendv2.service.IncidentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/accident_report")
public class IncidentReportController {
	private final IncidentReportService incidentReportService;

	@GetMapping
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(incidentReportService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(incidentReportService.getById(id));
	}

	@GetMapping("/user/{email}")
	public ResponseEntity<?> getAllByUser(@PathVariable String email){
		return ResponseEntity.ok(incidentReportService.getAllByUser(email));
	}

	@PostMapping
	public ResponseEntity<?> addAccidentReport(@RequestBody @Valid IncidentReportRequest incidentReportRequest){
		incidentReportService.add(incidentReportRequest);
		return ResponseEntity.ok("Accident Report added successfully");
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody @Valid AccidentReportUpdateRequest accidentReportUpdateRequest){
		incidentReportService.updateById(id, accidentReportUpdateRequest);
		return ResponseEntity.ok("Accident Report successfully updated");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		incidentReportService.deleteById(id);
		return ResponseEntity.ok("Accident Report successfully deleted");
	}
}
