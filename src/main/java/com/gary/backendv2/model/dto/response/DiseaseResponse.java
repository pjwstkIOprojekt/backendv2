package com.gary.backendv2.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder
public class DiseaseResponse {
	private Integer diseaseId;
	private String diseaseName;
	private String description;
	private boolean shareWithBand;
}
