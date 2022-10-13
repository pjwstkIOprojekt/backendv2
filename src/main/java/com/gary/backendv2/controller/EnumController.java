package com.gary.backendv2.controller;

import com.gary.backendv2.model.enums.AllergyType;
import com.gary.backendv2.model.enums.BloodType;
import com.gary.backendv2.model.enums.RhType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController()
@RequestMapping(path = "/enum")
@RequiredArgsConstructor
public class EnumController {
	@GetMapping("/allergy_type")
	public List<String> getAllergyTypeList(){
		return Stream.of(AllergyType.values())
				.map(AllergyType::name)
				.collect(Collectors.toList());
	}

	@GetMapping("/rh_type")
	public List<String> getRhTypeList(){
		return Stream.of(RhType.values())
				.map(RhType::name)
				.collect(Collectors.toList());
	}

	@GetMapping("/blood_type")
	public List<String> getBloodTypeList(){
		return Stream.of(BloodType.values())
				.map(BloodType::name)
				.collect(Collectors.toList());
	}
}
