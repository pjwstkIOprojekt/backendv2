package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.IncidentRequest;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/incident")
public class IncidentController {
	private final IncidentService incidentService;

	@GetMapping
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(incidentService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(incidentService.getById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid IncidentRequest incidentRequest){
		incidentService.update(id, incidentRequest);
		return ResponseEntity.ok("Incident updated successfully");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		incidentService.delete(id);
		return ResponseEntity.ok("Incident deleted successfully");
	}

	@PutMapping("/{id}/ambulance")
	public ResponseEntity<?> addAmbulances(@PathVariable Integer id, @RequestBody List<String> licencePlates){
		incidentService.addAmbulances(id, licencePlates);
		return ResponseEntity.ok("Ambulances assigned successfully to incident");
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<?> changeStatus(@PathVariable Integer id, @RequestBody IncidentStatusType incidentStatusType){
		incidentService.changeIncidentStatus(id, incidentStatusType);
		return ResponseEntity.ok("Status successfully changed");
	}
}
