package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.DiseaseRequest;
import com.gary.backendv2.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(path = "/disease")
@RequiredArgsConstructor
public class DiseaseController {
	private final DiseaseService diseaseService;

	@GetMapping("")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(diseaseService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return ResponseEntity.ok(diseaseService.getAllById(id));
	}

	@PostMapping("")
	public ResponseEntity<?> addDisease(@RequestBody @Valid DiseaseRequest diseaseRequest){
		diseaseService.addDisease(diseaseRequest);
		return ResponseEntity.ok("Disease added successfully");
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateDisease(@PathVariable Integer id, @RequestBody @Valid DiseaseRequest diseaseRequest){
		diseaseService.updateDisease(id, diseaseRequest);
		return ResponseEntity.ok("Disease updated");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeDisease(@PathVariable Integer id){
		diseaseService.removeDisease(id);
		return ResponseEntity.ok("Disease removed");
	}
}
