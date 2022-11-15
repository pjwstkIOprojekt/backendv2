package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.*;
import com.gary.backendv2.model.dto.request.TrustedPersonRequest;
import com.gary.backendv2.model.dto.response.AmbulanceResponse;
import com.gary.backendv2.model.dto.response.TrustedPersonResponse;
import com.gary.backendv2.repository.TrustedPersonRepository;
import com.gary.backendv2.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TrustPersonServiceTest {
    private final TrustedPersonRepository trustedPersonRepository = mock(TrustedPersonRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TrustedPersonService trustedPersonService = new TrustedPersonService(trustedPersonRepository, userRepository);


    @Test
    void getAllTrustedPersons() {
        List<TrustedPerson> expected = List.of(new TrustedPerson());

        when(trustedPersonRepository.findAll()).thenReturn(expected);


        var result = trustedPersonService.getAll();

        assertEquals(expected.size(), result.size());


    }

    @Test
    void getTrustedPersonByEmailShouldFind() {
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
    void getTrustedPersonByEmailShouldNotFind() {
        String email = "jan@op.pl";
        when(trustedPersonRepository.findByEmail(email)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpException.class, () -> {
            trustedPersonService.getByEmail(email);
        });
        String expectedMessage = String.format("Cannot find user with %s", email);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteTrustedPersonByEmail() {
        TrustedPersonRequest trustedPersonRequest = new TrustedPersonRequest();
        trustedPersonRequest.setUserEmail("tom@pjatk.pl");
        trustedPersonRequest.setFirstName("Tomasz");
        trustedPersonRequest.setLastName("Kowalski");
        trustedPersonRequest.setPhone("123456789");

        User user = new User();
        when(userRepository.findByEmail(trustedPersonRequest.getUserEmail())).thenReturn(Optional.of(user));
        trustedPersonService.addTrustedPerson(trustedPersonRequest);

        trustedPersonService.deleteByEmail("tom@pjatk.pl");

        verify(trustedPersonRepository, times(1)).delete(any(TrustedPerson.class));
    }

    @Test
    void addNoEmailShouldCreate() {
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
