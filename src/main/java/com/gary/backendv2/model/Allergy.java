package com.gary.backendv2.model;
import lombok.*;

import com.gary.backendv2.model.enums.AllergyType;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@Table(name = "Allergy")
@Entity
@AllArgsConstructor
@Getter
@Setter
public class Allergy {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer allergyId;

	private AllergyType allergyType;

	private String allergyName;

	private String other;

	@ManyToMany
	@JoinTable(name = "allergy_medical_infos",
			joinColumns = @JoinColumn(name = "allergy"),
			inverseJoinColumns = @JoinColumn(name = "medical_info"))
	private Set<MedicalInfo> medicalInfos = new LinkedHashSet<>();
}
