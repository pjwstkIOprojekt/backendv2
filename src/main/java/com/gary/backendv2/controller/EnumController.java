package com.gary.backendv2.controller;

import com.gary.backendv2.model.enums.AllergyType;
import com.gary.backendv2.model.enums.BloodType;
import com.gary.backendv2.model.enums.RhType;
import com.gary.backendv2.utils.EnumUtils;
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
		return EnumUtils.getEnumValues(AllergyType.class);
	}

	@GetMapping("/rh_type")
	public List<String> getRhTypeList(){
		return EnumUtils.getEnumValues(RhType.class);
	}

	@GetMapping("/blood_type")
	public List<String> getBloodTypeList(){
		return EnumUtils.getEnumValues(BloodType.class);
	}
}
