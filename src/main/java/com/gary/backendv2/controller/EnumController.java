package com.gary.backendv2.controller;

import com.gary.backendv2.model.enums.*;
import com.gary.backendv2.utils.EnumUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	@GetMapping("/ambulance_states")
	public List<String> getAmbulanceStatesList() {return EnumUtils.getEnumValues(AmbulanceStateType.class);}

	@GetMapping("/ambulance_classes")
	public List<String> getAmbulanceClasses() {return EnumUtils.getEnumValues(AmbulanceClass.class);}

	@GetMapping("/ambulance_types")
	public List<String> getAmbulanceTypes() {return EnumUtils.getEnumValues(AmbulanceType.class);}

	@GetMapping("/emergency_type")
	public List<String> getEmergencyTypes() { return EnumUtils.getEnumValues(EmergencyType.class);}

	@GetMapping("/facility_type")
	public List<String> getFacilityType() { return EnumUtils.getEnumValues(FacilityType.class);}

	@GetMapping("/roles")
	public List<String> getRoles() {return EnumUtils.getEnumValues(RoleName.class);}

	@GetMapping("/incident_status")
	public List<String> getIncidentStatuses() {return  EnumUtils.getEnumValues(IncidentStatusType.class);}
	@GetMapping("/tutorial_type")
	public List<String> getTutorialType() {return EnumUtils.getEnumValues(TutorialType.class);}
}
