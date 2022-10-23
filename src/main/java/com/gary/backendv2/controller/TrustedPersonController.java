package com.gary.backendv2.controller;

import com.gary.backendv2.service.TrustedPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "/trusted")
@RequiredArgsConstructor
public class TrustedPersonController {
	private final TrustedPersonService trustedPersonService;

	@GetMapping("")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(trustedPersonService.getAll());
	}
}
