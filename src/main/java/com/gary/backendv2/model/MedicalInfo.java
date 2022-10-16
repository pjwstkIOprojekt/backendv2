package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.BloodType;
import com.gary.backendv2.model.enums.RhType;
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

	@Enumerated(EnumType.STRING)
	private RhType rhType;

	@Enumerated(EnumType.STRING)
	private BloodType bloodType;

	@OneToOne(mappedBy = "medicalInfo")
	private User user;

	@ManyToMany(mappedBy = "medicalInfos", fetch = FetchType.EAGER)
	private Set<Allergy> allergies = new LinkedHashSet<>();

	@ManyToMany(mappedBy = "medicalInfos", fetch = FetchType.EAGER)
	private Set<Disease> diseases = new LinkedHashSet<>();
}