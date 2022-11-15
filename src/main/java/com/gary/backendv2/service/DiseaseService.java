package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.exception.NotFoundException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.model.Disease;
import com.gary.backendv2.model.MedicalInfo;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.DiseaseRequest;
import com.gary.backendv2.model.dto.response.DiseaseResponse;
import com.gary.backendv2.repository.DiseaseRepository;
import com.gary.backendv2.repository.MedicalInfoRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiseaseService {
	private final DiseaseRepository diseaseRepository;
	private final MedicalInfoRepository medicalInfoRepository;
	private final UserRepository userRepository;

	public List<DiseaseResponse> getAll(){
		List<Disease> diseases = diseaseRepository.findAll();
		List<DiseaseResponse> diseaseResponses = new ArrayList<>();
		for (Disease d : diseases) {
			diseaseResponses.add(
					DiseaseResponse
							.builder()
							.diseaseId(d.getDiseaseId())
							.diseaseName(d.getDiseaseName())
							.description(d.getDescription())
							.shareWithBand(d.isShareWithBand())
							.build()
			);
		}
		return diseaseResponses;
	}

	public DiseaseResponse getAllById(Integer id){
		Optional<Disease> optionalDisease = diseaseRepository.findByDiseaseId(id);
		if (optionalDisease.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find disease with %s", id));
		}
		Disease disease = optionalDisease.get();
		return DiseaseResponse.builder()
				.diseaseId(disease.getDiseaseId())
				.diseaseName(disease.getDiseaseName())
				.description(disease.getDescription())
				.shareWithBand(disease.isShareWithBand())
				.build();
	}

	public void removeDisease(Integer id){
		Optional<Disease> optionalDisease = diseaseRepository.findByDiseaseId(id);
		if (optionalDisease.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find disease with %s", id));
		}
		Disease disease = optionalDisease.get();
		diseaseRepository.delete(disease);
	}

	public void updateDisease(Integer id, DiseaseRequest diseaseRequest){
		Optional<Disease> optionalDisease = diseaseRepository.findByDiseaseId(id);
		if (optionalDisease.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find disease with %s", id));
		}
		Disease disease = optionalDisease.get();
		disease.setDiseaseName(diseaseRequest.getDiseaseName());
		disease.setDescription(diseaseRequest.getDescription());
		disease.setShareWithBand(diseaseRequest.getShareWithBand());
		diseaseRepository.save(disease);
	}

	public void addDisease(DiseaseRequest diseaseRequest){
		Optional<User> userOptional = userRepository.findByEmail(diseaseRequest.getUserEmail());
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", diseaseRequest.getUserEmail()));
		}
		MedicalInfo userMedialInfo = userOptional.get().getMedicalInfo();
		if (userMedialInfo.getDiseases().stream()
				.map(Disease::getDiseaseName)
				.anyMatch(x -> x.equals(diseaseRequest.getDiseaseName()))) {
			throw new HttpException(HttpStatus.BAD_REQUEST, String.format("%s user have %s disease", diseaseRequest.getUserEmail(), diseaseRequest.getDiseaseName()));
		}
		Disease disease = Disease.builder()
				.medicalInfos(new HashSet<>())
				.diseaseName(diseaseRequest.getDiseaseName())
				.description(diseaseRequest.getDescription())
				.shareWithBand(diseaseRequest.getShareWithBand()).build();

		userMedialInfo.getDiseases().add(disease);
		if (disease.getMedicalInfos()
				.stream()
				.map(MedicalInfo::getMedicalInfoId)
				.noneMatch(x -> x.equals(userMedialInfo.getMedicalInfoId()))) {
			disease.getMedicalInfos().add(userMedialInfo);
		}
		medicalInfoRepository.save(userMedialInfo);
		diseaseRepository.save(disease);
	}
}
