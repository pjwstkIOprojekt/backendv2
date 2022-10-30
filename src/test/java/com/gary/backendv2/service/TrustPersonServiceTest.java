package com.gary.backendv2.service;

import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.TrustedPerson;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.TrustedPersonRequest;
import com.gary.backendv2.repository.TrustedPersonRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TrustPersonServiceTest {
	private final TrustedPersonRepository trustedPersonRepository = mock(TrustedPersonRepository.class);
	private final UserRepository userRepository = mock(UserRepository.class);
	private final TrustedPersonService trustedPersonService = new TrustedPersonService(trustedPersonRepository, userRepository);

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
