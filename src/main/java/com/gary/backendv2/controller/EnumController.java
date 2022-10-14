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
	private static List<String> getEnumValues(Class<? extends Enum> cl) {
		return Stream.of(cl.getEnumConstants())
				.map(e -> e.name())
				.collect(Collectors.toList());
	}

	@GetMapping("/allergy_type")
	public List<String> getAllergyTypeList(){
		return getEnumValues(AllergyType.class);
	}

	@GetMapping("/rh_type")
	public List<String> getRhTypeList(){
		return getEnumValues(RhType.class);
	}

	@GetMapping("/blood_type")
	public List<String> getBloodTypeList(){
		return getEnumValues(BloodType.class);
	}
}
