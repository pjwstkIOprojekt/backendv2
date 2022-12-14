package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.BackupType;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.users.employees.AbstractEmployee;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Backup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer backupId;

	private Boolean accepted;

	private String justification;

	private LocalDateTime time;

	private BackupType backupType;

	@ManyToOne
	private AbstractEmployee requester;

	@ManyToOne
	private Incident incident;
}
