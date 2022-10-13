package com.gary.backendv2.service;

import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.BloodRequest;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalInfoService {
	private final MedicalInfoRepository medicalInfoRepository;
	private final UserRepository userRepository;

	public BloodRequest getBloodByMedicalId(Integer id){
		MedicalInfo medicalInfo = medicalInfoRepository.findByMedicalInfoId(id);
		BloodRequest bloodRequest = new BloodRequest();
		bloodRequest.setBloodType(medicalInfo.getBloodType());
		bloodRequest.setRhType(medicalInfo.getRhType());
		bloodRequest.setMedicalInfoId(medicalInfo.getMedicalInfoId());
		return bloodRequest;
	}

	public void addBlood (BloodRequest bloodRequest){
		MedicalInfo medicalInfo;
		if(bloodRequest.getMedicalInfoId() == null){
			User user = userRepository.getByUserId(bloodRequest.getUserId());
			medicalInfo = MedicalInfo.builder().user(user).build();
			user.setMedicalInfo(medicalInfo);
			userRepository.save(user);
		}else{
			medicalInfo = medicalInfoRepository.findByMedicalInfoId(bloodRequest.getMedicalInfoId());
		}
		medicalInfo.setBloodType(bloodRequest.getBloodType());
		medicalInfo.setRhType(bloodRequest.getRhType());
		medicalInfoRepository.save(medicalInfo);
	}

	public void removeBlood(Integer id){
		MedicalInfo medicalInfo = medicalInfoRepository.findByMedicalInfoId(id);
		medicalInfo.setBloodType(null);
		medicalInfo.setRhType(null);
		medicalInfoRepository.save(medicalInfo);
	}

	public void updateBlood(Integer id, BloodRequest bloodRequest){
		MedicalInfo medicalInfo = medicalInfoRepository.findByMedicalInfoId(id);
		medicalInfo.setBloodType(bloodRequest.getBloodType());
		medicalInfo.setRhType(bloodRequest.getRhType());
		medicalInfoRepository.save(medicalInfo);
	}
}
