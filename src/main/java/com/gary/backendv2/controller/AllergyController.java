package com.gary.backendv2.controller;

import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.service.AllergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
