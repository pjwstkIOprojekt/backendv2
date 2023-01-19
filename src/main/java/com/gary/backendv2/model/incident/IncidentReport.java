package com.gary.backendv2.model.incident;

import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.model.dto.request.IncidentReportRequest;
import com.gary.backendv2.model.enums.*;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.service.IncidentReportService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accidentId;

	private LocalDateTime date;

	@Embedded
	private Location location;

	private String address;

	private String bandCode;

	@Enumerated(EnumType.STRING)
	private EmergencyType emergencyType;



	@ManyToOne
	private User reporter;

	private boolean conscious;

	private boolean breathing;


	@OneToOne
	private Incident incident;

	private String description;


	public void accept(EntityVisitor ev, IncidentReportService incidentReportService, List<BaseRequest> baseRequest) {
		ev.visit(this, incidentReportService, baseRequest);
	}

}
