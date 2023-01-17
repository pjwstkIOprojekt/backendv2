package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.BloodRequest;
import com.gary.backendv2.service.MedicalInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController()
@RequestMapping(path = "/medical_info")
@RequiredArgsConstructor
public class MedicalInfoController {
	private final MedicalInfoService medicalInfoService;

	@GetMapping("/user/{email}")
	public ResponseEntity<?> getByUserEmail(@PathVariable @Email String email){
		return ResponseEntity.ok(medicalInfoService.getByUserEmail(email));
	}

	@GetMapping("/user/bandcode/{bandCode}")
	public ResponseEntity<?> getByBandCode(@PathVariable String bandCode) {
		return ResponseEntity.ok(medicalInfoService.getByBandCode(bandCode));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeMedicalInfo(@PathVariable Integer id){
		medicalInfoService.removeMedicalInfo(id);
		return ResponseEntity.ok("Removed medical info");
	}

	@GetMapping("/blood/{id}")
	public BloodRequest getBloodByMedicalId(@PathVariable Integer id){
		return medicalInfoService.getBloodByMedicalId(id);
	}

	@PostMapping("/blood")
	public ResponseEntity<?> addBlood(@RequestBody @Valid BloodRequest bloodRequest){
		medicalInfoService.addBlood(bloodRequest);
		return ResponseEntity.ok("Added blood");
	}

	@PutMapping("/blood/{id}")
	public ResponseEntity<?> updateBlood(@PathVariable Integer id, @RequestBody @Valid BloodRequest bloodRequest){
		medicalInfoService.updateBlood(id, bloodRequest);
		return ResponseEntity.ok("Blood updated");
	}

	@DeleteMapping("/blood/{id}")
	public ResponseEntity<?> deleteBlood(@PathVariable Integer id){
		medicalInfoService.removeBlood(id);
		return ResponseEntity.ok("Removed blood");
	}
}