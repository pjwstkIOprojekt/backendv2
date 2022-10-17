package com.gary.backendv2.service;

import com.gary.backendv2.exception.NotFoundException;
import com.gary.backendv2.model.Disease;
import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.DiseaseRequest;
import com.gary.backendv2.repository.DiseaseRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiseaseService {
	private final DiseaseRepository diseaseRepository;
	private final MedicalInfoRepository medicalInfoRepository;
	private final UserRepository userRepository;

	public List<Disease> getAll(){
		return diseaseRepository.findAll();
	}

	public Disease getAllById(Integer id){
		return diseaseRepository.findByDiseaseId(id);
	}

	public void removeDisease(Integer id){
		diseaseRepository.delete(diseaseRepository.findByDiseaseId(id));
	}

	public void updateDisease(Integer id, DiseaseRequest diseaseRequest){
		Disease disease = diseaseRepository.findByDiseaseId(id);
		disease.setDiseaseName(diseaseRequest.getDiseaseName());
		disease.setDescription(diseaseRequest.getDescription());
		diseaseRepository.save(disease);
	}

	public void addDisease(DiseaseRequest diseaseRequest){
		MedicalInfo medicalInfo;
		if(diseaseRequest.getMedicalInfoId() != null){
			medicalInfo = medicalInfoRepository.findByMedicalInfoId(diseaseRequest.getMedicalInfoId()).orElseThrow(() -> new NotFoundException("No record with that ID"));
		}else{
			User user = userRepository.getByUserId(diseaseRequest.getUserId());
			medicalInfo = MedicalInfo.builder()
					.user(user)
					.diseases(new HashSet<>())
					.allergies(new HashSet<>())
					.build();
			user.setMedicalInfo(medicalInfo);
			userRepository.save(user);
		}
		Disease disease = Disease.builder()
				.medicalInfos(new HashSet<>())
				.diseaseName(diseaseRequest.getDiseaseName())
				.description(diseaseRequest.getDescription()).build();

		disease.getMedicalInfos().add(medicalInfo);
		medicalInfo.getDiseases().add(disease);
		medicalInfoRepository.save(medicalInfo);
		diseaseRepository.save(disease);
	}
}
