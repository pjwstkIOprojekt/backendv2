package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.IncidentRequest;
import com.gary.backendv2.model.dto.request.VictimInfoRequest;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/incident")
public class IncidentController {
	private final IncidentService incidentService;

	@GetMapping
	@Operation(summary = "Get all incidents", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(incidentService.getAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get incident by incident ID", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(incidentService.getById(id));
	}

	@GetMapping("/status/{status}")
	@Operation(summary = "Get incidents with given status", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> getByStatus (@PathVariable IncidentStatusType status){
		return ResponseEntity.ok(incidentService.getByStatus(status));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update na incident", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid IncidentRequest incidentRequest){
		incidentService.update(id, incidentRequest);
		return ResponseEntity.ok("Incident updated successfully");
	}

	@GetMapping("/{id}/casualties")
	@Operation(summary = "Gets victims in an incident", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> getVictimsByIncidentId(@PathVariable Integer id) {
		return ResponseEntity.ok(incidentService.getVictimsInformation(id));
	}

	@PostMapping("/{id}/casualties")
	@Operation(summary = "Add casualty victim info", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> addVictimInfo(@Valid @RequestBody VictimInfoRequest victimInfoRequest, @PathVariable Integer id, Authentication authentication) {
		incidentService.addVictimInfo(id, victimInfoRequest, authentication);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/casualties/{victimInfoId}")
	@Operation(summary = "Edit casualty victim info", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> editVictimInfo(@Valid @RequestBody VictimInfoRequest victimInfoRequest, @PathVariable Integer id, @PathVariable Integer victimInfoId, Authentication authentication) {
		return ResponseEntity.ok(incidentService.updateVictimsInfo(id, victimInfoId, victimInfoRequest, authentication));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete an incident", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> delete(@PathVariable Integer id){
		incidentService.delete(id);
		return ResponseEntity.ok("Incident deleted successfully");
	}

	@PutMapping("/{id}/ambulance")
	@Operation(summary = "Assign ambulances to an incident", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> addAmbulances(@PathVariable Integer id, @RequestBody List<String> licencePlates){
		return ResponseEntity.ok(incidentService.addAmbulances(id, licencePlates));
	}

	@PostMapping("/{id}/status/{status}")
	@Operation(summary = "Change status of an incident", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> changeStatus(@PathVariable Integer id, @PathVariable IncidentStatusType status){
		incidentService.changeIncidentStatus(id, status);
		return ResponseEntity.ok("Status successfully changed");
	}
}
