package com.gary.backendv2.service;

import com.gary.backendv2.model.*;
import com.gary.backendv2.model.dto.request.TrustedPersonRequest;
import com.gary.backendv2.model.dto.response.AmbulanceResponse;
import com.gary.backendv2.model.dto.response.TrustedPersonResponse;
import com.gary.backendv2.repository.TrustedPersonRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TrustPersonServiceTest {
	private final TrustedPersonRepository trustedPersonRepository = mock(TrustedPersonRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final TrustedPersonService trustedPersonService = new TrustedPersonService(trustedPersonRepository, userRepository);


	@Test
	void getAllTrustedPersons(){
		List<TrustedPerson> expected = List.of(new TrustedPerson());

		when(trustedPersonRepository.findAll()).thenReturn(expected);


		var result = trustedPersonRepository.findAll();

		assertEquals(expected.size(), result.size());


	}

	@Test
	void getTrustedPersonByEmailShouldFind(){
		TrustedPersonRequest trustedPersonRequest = new TrustedPersonRequest();
		trustedPersonRequest.setUserEmail("tom@pjatk.pl");
		trustedPersonRequest.setFirstName("Tomasz");
		trustedPersonRequest.setLastName("Kowalski");
		trustedPersonRequest.setPhone("123456789");

		User user = new User();
		when(userRepository.findByEmail(trustedPersonRequest.getUserEmail())).thenReturn(Optional.of(user));
		trustedPersonService.addTrustedPerson(trustedPersonRequest);

		TrustedPersonResponse result = trustedPersonService.getByEmail("tom@pjatk.pl");


		assertNotNull(result);
		assertEquals(trustedPersonRequest.getEmail(), result.getEmail());

	}

	@Test
	void addNoEmailShouldCreate(){
		TrustedPersonRequest trustedPersonRequest = new TrustedPersonRequest();
		trustedPersonRequest.setUserEmail("test@test.pl");
		trustedPersonRequest.setFirstName("Tomasz");
		trustedPersonRequest.setLastName("Kowalski");
		trustedPersonRequest.setPhone("123456789");

		User user = new User();
		when(userRepository.findByEmail(trustedPersonRequest.getUserEmail())).thenReturn(Optional.of(user));
		trustedPersonService.addTrustedPerson(trustedPersonRequest);
		verify(trustedPersonRepository, times(1)).save(any(TrustedPerson.class));
	}
}
