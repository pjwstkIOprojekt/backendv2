package com.gary.backendv2.controller;

import com.gary.backendv2.model.enums.AllergyType;
import com.gary.backendv2.utils.EnumUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumControllerTest {
	private final EnumController enumController = new EnumController();

	@Test
	void getAllergy(){
		assertEquals(enumController.getAllergyTypeList(), EnumUtils.getEnumValues(AllergyType.class));
	}
}
