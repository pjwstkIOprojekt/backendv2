package com.gary.backendv2.service;

import com.gary.backendv2.model.TrustedPerson;
import com.gary.backendv2.model.dto.response.TrustedPersonResponse;
import com.gary.backendv2.repository.TrustedPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrustedPersonService {
	private final TrustedPersonRepository trustedPersonRepository;

	public List<TrustedPersonResponse> getAll(){
		List<TrustedPerson> trustedPeople = trustedPersonRepository.findAll();
		List<TrustedPersonResponse> trustedPersonResponses = new ArrayList<>();
		for (TrustedPerson t: trustedPeople) {
			trustedPersonResponses.add(
				TrustedPersonResponse
						.builder()
						.email(t.getEmail())
						.firstName(t.getFirstName())
						.trustedId(t.getTrustedId())
						.lastName(t.getLastName())
						.phone(t.getPhone())
						.build()
			);
		}
		return trustedPersonResponses;
	}
}
