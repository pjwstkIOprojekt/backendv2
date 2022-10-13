package com.gary.backendv2.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "medical_info")
@Entity
@Getter
@Setter
public class MedicalInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer medicalInfoId;


	@OneToOne(mappedBy = "medicalInfo")
	private User user;

	@ManyToMany(mappedBy = "medicalInfos", fetch = FetchType.EAGER)
	private Set<Allergy> allergies = new LinkedHashSet<>();
}