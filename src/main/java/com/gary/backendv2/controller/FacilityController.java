package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/facility")
public class FacilityController {
	private final FacilityService facilityService;

	@GetMapping
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(facilityService.getAll());
	}

	@GetMapping("/id")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(facilityService.getById(id));
	}

	@DeleteMapping("/id")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		facilityService.delete(id);
		return ResponseEntity.ok("Facility successfully deleted");
	}

	@PostMapping
	public ResponseEntity<?> add(@RequestBody @Valid FacilityRequest facilityRequest){
		facilityService.add(facilityRequest);
		return ResponseEntity.ok("Facility successfully added");
	}

	@PutMapping("/id")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid FacilityRequest facilityRequest){
		facilityService.update(id,facilityRequest);
		return ResponseEntity.ok("Facility updated successfully");
	}
}
