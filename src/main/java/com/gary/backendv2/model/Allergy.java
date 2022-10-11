package com.gary.backendv2.model;
import lombok.*;

import com.gary.backendv2.types.AllergyType;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@Table(name = "Allergy")
@Entity
@AllArgsConstructor
public class Allergy {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer allergyId;

	private AllergyType allergyType;

	private String allergyName;

	private String other;

	@ManyToOne
	private MedicalInfo medicalInfo;
}
