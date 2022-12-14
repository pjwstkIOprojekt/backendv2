package com.gary.backendv2.controller;

import com.gary.backendv2.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/backup")
@RequiredArgsConstructor
public class BackupController {
	private BackupService backupService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(backupService.getById(id));
	}
}
