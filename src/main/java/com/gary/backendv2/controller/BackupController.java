package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.BackupAddRequest;
import com.gary.backendv2.model.dto.request.BackupUpdateRequest;
import com.gary.backendv2.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping(path = "/backup")
@RequiredArgsConstructor
public class BackupController {
	private BackupService backupService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(backupService.getById(id));
	}

	@GetMapping
	public ResponseEntity<?> getAll(){ return ResponseEntity.ok(backupService.getAll()); }

	@PostMapping
	public ResponseEntity<?> add (@RequestBody @Valid BackupAddRequest backupAddRequest){
		backupService.add(backupAddRequest);
		return ResponseEntity.ok("Response successfully updated");
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody @Valid BackupUpdateRequest backupUpdateRequest){
		backupService.update(id, backupUpdateRequest);
		return ResponseEntity.ok("Backup successfully updated");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		backupService.delete(id);
		return ResponseEntity.ok("Backup successfully deleted.");
	}
}
