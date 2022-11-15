package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.IncidentStateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Incident {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer incidentId;
	private Integer dangerScale;
	@Enumerated(EnumType.STRING)
	private IncidentStateType incidentStateType;
	private String reactionJustification;
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "incident")
	private AccidentReport accidentReport;
	@ManyToOne(fetch = FetchType.EAGER)
	private Dispatcher dispatcher;
	@ManyToMany(mappedBy = "incidents")
	private Set<Ambulance> ambulances;
}
