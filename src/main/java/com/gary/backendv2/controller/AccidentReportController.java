package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.AccidentReportRequest;
import com.gary.backendv2.model.dto.request.AccidentReportUpdateRequest;
import com.gary.backendv2.service.AccidentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/accident_report")
public class AccidentReportController {
	private final AccidentReportService accidentReportService;

	@GetMapping
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(accidentReportService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(accidentReportService.getById(id));
	}

	@PostMapping
	public ResponseEntity<?> addAccidentReport(@RequestBody @Valid AccidentReportRequest accidentReportRequest){
		accidentReportService.add(accidentReportRequest);
		return ResponseEntity.ok("Accident Report added successfully");
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody @Valid AccidentReportUpdateRequest accidentReportUpdateRequest){
		accidentReportService.updateById(id, accidentReportUpdateRequest);
		return ResponseEntity.ok("Accident Report successfully updated");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		accidentReportService.deleteById(id);
		return ResponseEntity.ok("Accident Report successfully deleted");
	}
}
