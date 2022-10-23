package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.TrustedPersonRequest;
import com.gary.backendv2.service.TrustedPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController()
@RequestMapping(path = "/trusted")
@RequiredArgsConstructor
public class TrustedPersonController {
	private final TrustedPersonService trustedPersonService;

	@GetMapping("")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(trustedPersonService.getAll());
	}

	@GetMapping("/{email}")
	public ResponseEntity<?> getByEmail(@PathVariable @Email String email){
		return ResponseEntity.ok(trustedPersonService.getByEmail(email));
	}

	@DeleteMapping("/{email}")
	public ResponseEntity<?> deleteByEmail(@PathVariable @Email String email){
		trustedPersonService.deleteByEmail(email);
		return ResponseEntity.ok("Trusted person deleted successfully.");
	}

	@PostMapping("")
	public ResponseEntity<?> addTrustedPerson(@RequestBody @Valid TrustedPersonRequest trustedPersonRequest){
		trustedPersonService.addTrustedPerson(trustedPersonRequest);
		return ResponseEntity.ok("Trusted person added successfully");
	}

	@PutMapping("")
	public ResponseEntity<?> updateTrustedPerson(@RequestBody @Valid TrustedPersonRequest trustedPersonRequest){
		trustedPersonService.updateTrustedPerson(trustedPersonRequest);
		return ResponseEntity.ok("Trusted person updated successfully");
	}
}
