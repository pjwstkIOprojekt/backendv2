package com.gary.backendv2.model;

import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.model.enums.FacilityType;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facility  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer facilityId;

	private String name;

	@Embedded
	private Location location;

	@Enumerated(EnumType.STRING)
	private FacilityType facilityType;

	public Facility create(FacilityRequest facilityRequest) {
		Facility facility = new Facility();
		facility.setFacilityId(null);
		facility.setFacilityType(facilityRequest.getFacilityType());
		facility.setName(facilityRequest.getName());
		facility.setLocation(Location.of(facilityRequest.getLongitude(), facilityRequest.getLatitude()));

		return facility;
	}

	public void accept(EntityVisitor ev, List<BaseRequest> baseRequest) {
		ev.visit(this, baseRequest);
	}
}
