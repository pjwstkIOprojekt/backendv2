package com.gary.backendv2.model;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "trusted")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrustedPerson {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer trustedId;

	private String firstName;

	private String lastName;

	@Email
	private String email;

	private String phone;

	@OneToOne(fetch = FetchType.EAGER)
	private User user;
}
