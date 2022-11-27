package com.gary.backendv2.controller;

import com.gary.backendv2.model.enums.*;
import com.gary.backendv2.utils.EnumUtils;
import org.junit.jupiter.api.Test;

import java.sql.Blob;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumControllerTest {
	private final EnumController enumController = new EnumController();

	@Test
	void getAllergy(){
		assertEquals(enumController.getAllergyTypeList(), EnumUtils.getEnumValues(AllergyType.class));
	}

	@Test
	void getAmbulanceClass(){assertEquals(enumController.getAmbulanceClasses(), EnumUtils.getEnumValues(AmbulanceClass.class));}

	@Test
	void getAmbulanceStateType() {assertEquals(enumController.getAmbulanceStatesList(), EnumUtils.getEnumValues(AmbulanceStateType.class));}

	@Test
	void getAmbulanceType () {assertEquals(enumController.getAmbulanceTypes(), EnumUtils.getEnumValues(AmbulanceType.class));}

	@Test
	void getBloodType() {assertEquals(enumController.getBloodTypeList(), EnumUtils.getEnumValues(BloodType.class));}

	@Test
	void getEmergencyType() {assertEquals(enumController.getEmergencyTypes(), EnumUtils.getEnumValues(EmergencyType.class));}

	@Test
	void getFacilityType() {assertEquals(enumController.getFacilityType(), EnumUtils.getEnumValues(FacilityType.class));}

	@Test
	void getRhType() {assertEquals(enumController.getRhTypeList(), EnumUtils.getEnumValues(RhType.class));}

	@Test
	void getRoles() {assertEquals(enumController.getRoles(), EnumUtils.getEnumValues(RoleName.class));}
	
	@Test
	void getIncidentStatuses() {assertEquals(enumController.getIncidentStatuses(), EnumUtils.getEnumValues(IncidentStatusType.class));}
}
