package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.FacilityType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer facilityId;

	@Embedded
	private Location location;

	@Enumerated(EnumType.STRING)
	private FacilityType facilityType;
}
