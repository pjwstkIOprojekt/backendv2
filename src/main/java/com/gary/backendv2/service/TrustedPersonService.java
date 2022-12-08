package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.TrustedPerson;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.dto.request.TrustedPersonRequest;
import com.gary.backendv2.model.dto.response.TrustedPersonResponse;
import com.gary.backendv2.repository.TrustedPersonRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrustedPersonService {
	private final TrustedPersonRepository trustedPersonRepository;
	private final UserRepository userRepository;

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

	public TrustedPersonResponse getByEmail(String userEmail){
		Optional<User> userOptional = userRepository.findByEmail(userEmail);
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", userEmail));
		}
		TrustedPerson trustedPerson = userOptional.get().getTrustedPerson();
		if(trustedPerson == null){
			throw new HttpException(HttpStatus.NO_CONTENT, String.format("User %s don't have trusted person", userEmail));
		}
		return  TrustedPersonResponse
				.builder()
				.phone(trustedPerson.getPhone())
				.email(trustedPerson.getEmail())
				.lastName(trustedPerson.getLastName())
				.firstName(trustedPerson.getFirstName())
				.trustedId(trustedPerson.getTrustedId())
				.build();
	}

	public void deleteByEmail(String userEmail){
		Optional<User> userOptional = userRepository.findByEmail(userEmail);
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", userEmail));
		}
		TrustedPerson trustedPerson = userOptional.get().getTrustedPerson();
		if(trustedPerson == null){
			throw new HttpException(HttpStatus.NO_CONTENT, String.format("User %s don't have trusted person", userEmail));
		}
		trustedPersonRepository.delete(trustedPerson);
		User user = userOptional.get();
		user.setTrustedPerson(null);
		userRepository.save(user);
	}

	public void addTrustedPerson(TrustedPersonRequest trustedPersonRequest){
		Optional<User> userOptional = userRepository.findByEmail(trustedPersonRequest.getUserEmail());
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", trustedPersonRequest.getUserEmail()));
		}
		User user = userOptional.get();
		if(user.getTrustedPerson() != null){
			throw new HttpException(HttpStatus.BAD_REQUEST, String.format("%s already have trusted person", trustedPersonRequest.getUserEmail()));
		}
		TrustedPerson trustedPerson = TrustedPerson.builder()
				.firstName(trustedPersonRequest.getFirstName())
				.lastName(trustedPersonRequest.getLastName())
				.email(trustedPersonRequest.getEmail())
				.phone(trustedPersonRequest.getPhone())
				.user(user)
				.build();

		user.setTrustedPerson(trustedPerson);
		trustedPersonRepository.save(trustedPerson);
		userRepository.save(user);
	}

	public void updateTrustedPerson(TrustedPersonRequest trustedPersonRequest){
		Optional<User> userOptional = userRepository.findByEmail(trustedPersonRequest.getUserEmail());
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", trustedPersonRequest.getUserEmail()));
		}
		TrustedPerson trustedPerson = userOptional.get().getTrustedPerson();
		if(trustedPerson == null){
			throw new HttpException(HttpStatus.NO_CONTENT, String.format("User %s don't have trusted person", trustedPersonRequest.getUserEmail()));
		}
		trustedPerson.setEmail(trustedPersonRequest.getEmail());
		trustedPerson.setFirstName(trustedPersonRequest.getFirstName());
		trustedPerson.setLastName(trustedPersonRequest.getLastName());
		trustedPerson.setPhone(trustedPersonRequest.getPhone());
		trustedPersonRepository.save(trustedPerson);
	}
}
