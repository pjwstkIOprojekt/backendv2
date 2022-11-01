package com.gary.backendv2.controller;

import com.gary.backendv2.service.AccidentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/accident_report")
public class AccidentReportController {
	private final AccidentReportService accidentReportService;
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		accidentReportService.deleteById(id);
		return ResponseEntity.ok("Accident Report successfully deleted");
	}
}
