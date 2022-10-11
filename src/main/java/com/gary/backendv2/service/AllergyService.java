package com.gary.backendv2.service;

import com.gary.backendv2.exception.NotFoundException;
import com.gary.backendv2.model.Allergy;
import com.gary.backendv2.repository.AllergyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllergyService {

	@Autowired
	private final AllergyRepository allergyRepository;

	public List<Allergy> getAll(){
		return allergyRepository.findAll();
	}

	public Allergy getById(Integer id){
		return allergyRepository.findById(id).orElseThrow(()-> new NotFoundException("No record with that ID"));
	}
}
