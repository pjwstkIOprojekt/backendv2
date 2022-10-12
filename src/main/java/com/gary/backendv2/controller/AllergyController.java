package com.gary.backendv2.controller;

import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.dto.request.AllergyRequest;
import com.gary.backendv2.service.AllergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController()
@RequestMapping(path = "/allergy")
@RequiredArgsConstructor
public class AllergyController {

	private final AllergyService allergyService;

	@GetMapping("")
	public List<Allergy> getAllAllergies(){
		return allergyService.getAll();
	}

	@GetMapping("/{id}")
	public Allergy getAllergyById(@PathVariable Integer id){return allergyService.getById(id);}

	@PostMapping("")
	public ResponseEntity<?> addAllergy(@RequestBody AllergyRequest allergyRequest){
		allergyService.addAllergy(allergyRequest);
		return ResponseEntity.ok("Allergy added successfully");
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAllergy(@PathVariable Integer id, @RequestBody AllergyRequest allergyRequest){
		allergyService.updateAllergy(id, allergyRequest);
		return ResponseEntity.ok("Allergy updated successfully");
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeAllergy(@PathVariable Integer id){
		allergyService.removeAllergy(id);
		return ResponseEntity.ok("Allergy removed successfully");
	}
}
