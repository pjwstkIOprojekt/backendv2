package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.*;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.response.AccidentReportResponse;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.repository.AccidentReportRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccidentReportService {
	private final AccidentReportRepository accidentReportRepository;
	private final UserRepository userRepository;

	public List<AccidentReportResponse> getAll(){
		List<AccidentReportResponse> accidentReportResponses = new ArrayList<>();
		for (AccidentReport accidentReport:accidentReportRepository.findAll()) {
			accidentReportResponses.add(
					AccidentReportResponse
							.builder()
							.bandCode(accidentReport.getBandCode())
							.consciousness(accidentReport.isConscious())
							.breathing(accidentReport.isBreathing())
							.emergencyType(accidentReport.getEmergencyType())
							.accidentId(accidentReport.getAccidentId())
							.location(accidentReport.getLocation())
							.victimCount(accidentReport.getVictimCount())
							.date(accidentReport.getDate())
							.description(accidentReport.getDescription())
							.build()
			);
		}
		return accidentReportResponses;
	}

	public List<AccidentReportResponse> getAllByUser(String email){
		List<AccidentReportResponse> accidentReportResponses = new ArrayList<>();
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", email));
		}
		for (AccidentReport accidentReport : userOptional.get().getAccidentReports()) {
			accidentReportResponses.add(
					AccidentReportResponse
							.builder()
							.bandCode(accidentReport.getBandCode())
							.consciousness(accidentReport.isConscious())
							.breathing(accidentReport.isBreathing())
							.emergencyType(accidentReport.getEmergencyType())
							.accidentId(accidentReport.getAccidentId())
							.location(accidentReport.getLocation())
							.victimCount(accidentReport.getVictimCount())
							.date(accidentReport.getDate())
							.description(accidentReport.getDescription())
							.build()
			);
		}
		return accidentReportResponses;
	}

	public AccidentReportResponse getById(Integer id){
		Optional<AccidentReport> accidentReportOptional = accidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Accident report with id %s not found", id));
		AccidentReport accidentReport = accidentReportOptional.get();
		return AccidentReportResponse
				.builder()
				.date(accidentReport.getDate())
				.accidentId(accidentReport.getAccidentId())
				.victimCount(accidentReport.getVictimCount())
				.location(accidentReport.getLocation())
				.emergencyType(accidentReport.getEmergencyType())
				.breathing(accidentReport.isBreathing())
				.consciousness(accidentReport.isConscious())
				.bandCode(accidentReport.getBandCode())
				.description(accidentReport.getDescription())
				.build();
	}
	
	public void add(AccidentReportRequest accidentReportRequest) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User reporter = null;
		if (!principal.equals("anonymousUser")) {
			UserPrincipal loggedPrincipal = (UserPrincipal) principal;
			reporter = userRepository.getByEmail(loggedPrincipal.getUsername());
		}

		accidentReportRepository.save(
				AccidentReport
						.builder()
						.reporter(reporter)
						.bandCode(accidentReportRequest.getBandCode())
						.breathing(accidentReportRequest.getBreathing())
						.conscious(accidentReportRequest.getConcious())
						.date(LocalDateTime.now())
						.description(accidentReportRequest.getDescription())
						.emergencyType(accidentReportRequest.getEmergencyType())
						.victimCount(accidentReportRequest.getVictimCount())
						.location(Location.of(accidentReportRequest.getLongitude(), accidentReportRequest.getLatitude()))
						.build()
		);
	}

	public void updateById(Integer id, AccidentReportUpdateRequest accidentReportUpdateRequest){
		Optional<AccidentReport> accidentReportOptional = accidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Accident report with id %s not found", id));
		AccidentReport accidentReport = accidentReportOptional.get();
		accidentReport.setBreathing(accidentReportUpdateRequest.isBreathing());
		accidentReport.setDescription(accidentReportUpdateRequest.getDescription());
		accidentReport.setBandCode(accidentReportUpdateRequest.getBandCode());
		accidentReport.setConscious(accidentReportUpdateRequest.isConsciousness());
		accidentReport.setEmergencyType(accidentReportUpdateRequest.getEmergencyType());
		accidentReport.setVictimCount(accidentReportUpdateRequest.getVictimCount());
		accidentReport.setLocation(Location.of(accidentReportUpdateRequest.getLongitude(), accidentReportUpdateRequest.getLatitude()));
		accidentReportRepository.save(accidentReport);
	}

	public void deleteById(Integer id){
		Optional<AccidentReport> accidentReportOptional = accidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Accident report with id %s not found", id));
		accidentReportRepository.delete(accidentReportOptional.get());
	}
}
