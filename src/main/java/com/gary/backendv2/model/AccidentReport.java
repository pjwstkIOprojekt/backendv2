package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccidentReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accidentId;

	private LocalDateTime date;

	@Embedded
	private Location location;

	private String address;

	private String bandCode;

	private EmergencyType emergencyType;

	private int victimCount;

	@ManyToOne
	private User reporter;

	private boolean conscious;

	private boolean breathing;


	@OneToOne
	private Incident incident;

	private String description;

}
