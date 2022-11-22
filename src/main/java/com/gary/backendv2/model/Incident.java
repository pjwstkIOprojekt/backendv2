package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.IncidentStatusType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer incidentId;
	private Integer dangerScale;
	@Enumerated(EnumType.STRING)
	private IncidentStatusType incidentStatusType;
	private String reactionJustification;
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "incident")
	private AccidentReport accidentReport;
	@ManyToOne(fetch = FetchType.EAGER)
	private Dispatcher dispatcher;
	@ManyToMany(mappedBy = "incidents")
	private Set<Ambulance> ambulances;
}
