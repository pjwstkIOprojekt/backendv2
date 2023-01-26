package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.BackupAddRequest;
import com.gary.backendv2.model.dto.request.BackupUpdateRequest;
import com.gary.backendv2.service.BackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping(path = "/backup")
@RequiredArgsConstructor
public class BackupController {
	private final BackupService backupService;

	@GetMapping("/{id}")
	@Operation(summary = "Gets backup by id", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(backupService.getById(id));
	}

	@GetMapping
	@Operation(summary = "Gets all backups", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> getAll(){ return ResponseEntity.ok(backupService.getAll()); }

	@PostMapping
	@Operation(summary = "Adds new backup to an incident", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> add (@RequestBody @Valid BackupAddRequest backupAddRequest){
		backupService.add(backupAddRequest);
		return ResponseEntity.ok("Response successfully updated");
	}

	@PutMapping("/{id}")
	@Operation(summary = "Updates backup with given id", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid BackupUpdateRequest backupUpdateRequest){
		backupService.update(id, backupUpdateRequest);
		return ResponseEntity.ok("Backup successfully updated");
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deletes backup with given id", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<?> delete(@PathVariable Integer id){
		backupService.delete(id);
		return ResponseEntity.ok("Backup successfully deleted.");
	}
}
