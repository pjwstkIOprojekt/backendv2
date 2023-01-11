package com.gary.backendv2.model.incident;

import com.gary.backendv2.listener.IncidentStatusTypeListener;
import com.gary.backendv2.model.CasualtyReport;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.model.users.employees.Dispatcher;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(IncidentStatusTypeListener.class)
public class Incident {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer incidentId;
	private int victimCount;
	private Integer dangerScale;
	@Enumerated(EnumType.STRING)
	private IncidentStatusType incidentStatusType;
	private String reactionJustification;
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "incident")
	private IncidentReport incidentReport;
	@ManyToOne(fetch = FetchType.EAGER)
	private Dispatcher dispatcher;
	@ManyToMany(mappedBy = "incidents")
	private Set<Ambulance> ambulances;
	@OneToOne
	private CasualtyReport casualtyReport;
}
