package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.exception.NotFoundException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.AllergyRequest;
import com.gary.backendv2.model.dto.response.AllergyResponse;
import com.gary.backendv2.repository.AllergyRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AllergyService {
	private final AllergyRepository allergyRepository;
	private final MedicalInfoRepository medicalInfoRepository;
	private final UserRepository userRepository;

	public List<AllergyResponse> getAll(){
		List<Allergy> allergies = allergyRepository.findAll();
		List <AllergyResponse> allergyResponses = new ArrayList<>();
		for (Allergy a : allergies) {
			allergyResponses.add(AllergyResponse.builder()
					.allergyId(a.getAllergyId())
					.allergyType(a.getAllergyType())
					.allergyName(a.getAllergyName())
					.other(a.getOther())
					.build());
		}
		return allergyResponses;
	}

	public AllergyResponse getById(Integer id){
		Optional<Allergy> allergyOptional = allergyRepository.findByAllergyId(id);
		if (allergyOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find allergy with %s", id));
		}
		Allergy allergy = allergyOptional.get();
		return AllergyResponse.builder()
				.allergyId(allergy.getAllergyId())
				.allergyName(allergy.getAllergyName())
				.allergyType(allergy.getAllergyType())
				.other(allergy.getOther())
				.build();
	}

	public void addAllergy(AllergyRequest allergyRequest){
		Optional<User> userOptional = userRepository.findByEmail(allergyRequest.getUserEmail());
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", allergyRequest.getUserEmail()));
		}
		MedicalInfo userMedialInfo = userOptional.get().getMedicalInfo();
		if (userMedialInfo.getAllergies().stream()
				.map(Allergy::getAllergyName)
				.anyMatch(x -> x.equals(allergyRequest.getAllergyName()))) {
			throw new HttpException(HttpStatus.BAD_REQUEST, String.format("%s already have %s allergy", allergyRequest.getUserEmail(), allergyRequest.getAllergyName()));
		}

		Optional<Allergy> allergyOptional = allergyRepository.findByAllergyName(allergyRequest.getAllergyName());
		Allergy allergy;
		if (allergyOptional.isEmpty()) {
			allergy = new Allergy();
			allergy.setAllergyType(allergyRequest.getAllergyType());
			allergy.setAllergyName(allergyRequest.getAllergyName());
			allergy.setOther(allergyRequest.getOther());
		} else allergy = allergyRepository.getByAllergyName(allergyRequest.getAllergyName());

		userMedialInfo.getAllergies().add(allergy);
		if (allergy.getMedicalInfos()
				.stream()
				.map(MedicalInfo::getMedicalInfoId)
				.noneMatch(x -> x.equals(userMedialInfo.getMedicalInfoId()))) {
			allergy.getMedicalInfos().add(userMedialInfo);
		}

		allergyRepository.save(allergy);
		medicalInfoRepository.save(userMedialInfo);
	}
	public void updateAllergy(Integer id, AllergyRequest allergyRequest){
		Allergy allergy = allergyRepository.findByAllergyId(id).orElseThrow();
		allergy.setAllergyName(allergyRequest.getAllergyName());
		allergy.setAllergyType(allergyRequest.getAllergyType());
		allergy.setOther(allergyRequest.getOther());
		allergyRepository.save(allergy);
	}
	public void removeAllergy(Integer id){
		allergyRepository.delete(allergyRepository.findByAllergyId(id).orElseThrow(()-> new NotFoundException("No record with that ID")));
	}
}
