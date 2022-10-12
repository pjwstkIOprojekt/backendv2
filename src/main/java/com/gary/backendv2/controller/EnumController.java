package com.gary.backendv2.controller;

import com.gary.backendv2.model.enums.AllergyType;
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
	public List<String> getEmergencyTypeList(){
		return Stream.of(AllergyType.values())
				.map(AllergyType::name)
				.collect(Collectors.toList());
	}
}
