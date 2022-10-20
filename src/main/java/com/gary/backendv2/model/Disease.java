package com.gary.backendv2.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@Table(name = "Disease")
@Entity
@AllArgsConstructor
@Getter
@Setter
public class Disease {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer diseaseId;

	@Column(nullable = false, unique = true)
	private String diseaseName;

	private String description;

	private boolean shareWithBand;

	@ManyToMany
	@JoinTable(name = "disease_medical_infos",
			joinColumns = @JoinColumn(name = "disease"),
			inverseJoinColumns = @JoinColumn(name = "medical_info"))
	private Set<MedicalInfo> medicalInfos = new LinkedHashSet<>();
}
