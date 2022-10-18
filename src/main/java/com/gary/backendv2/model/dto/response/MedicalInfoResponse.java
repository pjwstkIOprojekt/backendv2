package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.BloodType;
import com.gary.backendv2.model.enums.RhType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class MedicalInfoResponse {
	private Integer medicalInfoId;
	private RhType rhType;
	private BloodType bloodType;
	private List<AllergyResponse> allergies;
	private List<DiseaseResponse> diseases;
}
