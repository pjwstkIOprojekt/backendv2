package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.EmergencyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
public class AccidentReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accidentId;

	private LocalDate date;

	@Embedded
	private Location location;

	private String bandCode;

	private EmergencyType emergencyType;

	private int victimCount;

	@ManyToOne
	private User user;

	private boolean consciousness;

	private boolean breathing;
}
