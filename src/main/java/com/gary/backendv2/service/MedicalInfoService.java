package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.BloodRequest;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalInfoService {
	private final MedicalInfoRepository medicalInfoRepository;
	private final UserRepository userRepository;

	public List<MedicalInfo> getAll(){
		return medicalInfoRepository.findAll();
	}

	public MedicalInfo getByMedicalId(Integer id){
		return medicalInfoRepository.findByMedicalInfoId(id)
				.orElseThrow(()->new HttpException(HttpStatus.NOT_FOUND,  String.format("Cannot find medical info with id %s", id)));
	}

	public MedicalInfo getByUserEmail(String email){
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", email));
		}
		return userOptional.get().getMedicalInfo();
	}

	public void removeMedicalInfo(Integer id){
		medicalInfoRepository.delete(
				medicalInfoRepository
						.findByMedicalInfoId(id)
						.orElseThrow(()->new HttpException(HttpStatus.NOT_FOUND,  String.format("Cannot find medical info with id %s", id)))
		);
	}

	public BloodRequest getBloodByMedicalId(Integer id){
		Optional<MedicalInfo> medicalInfoOptional = medicalInfoRepository.findByMedicalInfoId(id);
		if (medicalInfoOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find medical info with id %s", id));
		}
		MedicalInfo medicalInfo = medicalInfoOptional.get();

		BloodRequest bloodRequest = new BloodRequest();
		bloodRequest.setBloodType(medicalInfo.getBloodType());
		bloodRequest.setRhType(medicalInfo.getRhType());
		bloodRequest.setUserEmail(medicalInfo.getUser().getEmail());
		return bloodRequest;
	}

	public void addBlood(BloodRequest bloodRequest) {
		Optional<User> userOptional = userRepository.findByEmail(bloodRequest.getUserEmail());
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", bloodRequest.getUserEmail()));
		}
		MedicalInfo userMedialInfo = userOptional.get().getMedicalInfo();

		userMedialInfo.setBloodType(bloodRequest.getBloodType());
		userMedialInfo.setRhType(bloodRequest.getRhType());
		medicalInfoRepository.save(userMedialInfo);
	}

	public void removeBlood(Integer id){
		Optional<MedicalInfo> medicalInfoOptional = medicalInfoRepository.findByMedicalInfoId(id);
		if (medicalInfoOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND,  String.format("Cannot find medical info with id %s", id));
		}
		MedicalInfo medicalInfo = medicalInfoOptional.get();
		medicalInfo.setBloodType(null);
		medicalInfo.setRhType(null);
		medicalInfoRepository.save(medicalInfo);
	}

	public void updateBlood(Integer id, BloodRequest bloodRequest){
		Optional<MedicalInfo> medicalInfoOptional = medicalInfoRepository.findByMedicalInfoId(id);
		if (medicalInfoOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND,  String.format("Cannot find medical info with id %s", id));
		}
		MedicalInfo medicalInfo = medicalInfoOptional.get();
		medicalInfo.setBloodType(bloodRequest.getBloodType());
		medicalInfo.setRhType(bloodRequest.getRhType());
		medicalInfoRepository.save(medicalInfo);
	}
}
