package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.BloodRequest;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalInfoService {
	private final MedicalInfoRepository medicalInfoRepository;
	private final UserRepository userRepository;

	public BloodRequest getBloodByMedicalId(Integer id){
		Optional<MedicalInfo> medicalInfoOptional = medicalInfoRepository.findByMedicalInfoId(id);
		if (medicalInfoOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find medical info with id %s", id));
		}
		MedicalInfo medicalInfo = medicalInfoOptional.get();

		BloodRequest bloodRequest = new BloodRequest();
		bloodRequest.setBloodType(medicalInfo.getBloodType());
		bloodRequest.setRhType(medicalInfo.getRhType());
		bloodRequest.setMedicalInfoId(medicalInfo.getMedicalInfoId());
		return bloodRequest;
	}

	public void addBlood(BloodRequest bloodRequest) {
		Optional<MedicalInfo> medicalInfo;
		if (bloodRequest.getMedicalInfoId() == null) {
			User user = userRepository.getByUserId(bloodRequest.getUserId());
			MedicalInfo mf = MedicalInfo
					.builder()
					.user(user)
					.build();

			user.setMedicalInfo(mf);

			medicalInfo = Optional.of(mf);
			userRepository.save(user);
		} else {
			medicalInfo = medicalInfoRepository.findByMedicalInfoId(bloodRequest.getMedicalInfoId());
		}
		if (medicalInfo.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND,  String.format("Cannot find medical info with id %s", bloodRequest.getMedicalInfoId()));
		}

		medicalInfo.get().setBloodType(bloodRequest.getBloodType());
		medicalInfo.get().setRhType(bloodRequest.getRhType());
		medicalInfoRepository.save(medicalInfo.get());
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
