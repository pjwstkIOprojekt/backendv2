package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.model.Ambulance;
import com.gary.backendv2.repository.AccidentReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccidentReportService {
	private final AccidentReportRepository accidentReportRepository;

	public void deleteById(Integer id){
		Optional<AccidentReport> accidentReportOptional = accidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Accident report with id %s not found", id));
		accidentReportRepository.delete(accidentReportOptional.get());
	}
}
