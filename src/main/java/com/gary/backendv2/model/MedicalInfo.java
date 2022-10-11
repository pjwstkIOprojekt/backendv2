package com.gary.backendv2.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medical_info")
@Entity
public class MedicalInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer medicalInfoId;

	@OneToMany(mappedBy = "medicalInfo")
	private Set<Allergy> allergies;
}