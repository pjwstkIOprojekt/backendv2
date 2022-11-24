package com.gary.backendv2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dispatcher extends AbstractEmployee {
	private  Integer openIncidents;

	@OneToMany(mappedBy = "dispatcher")
	private Set<Incident> incidents = new LinkedHashSet<>();
}
