package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.*;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.response.AccidentReportResponse;
import com.gary.backendv2.model.dto.response.geocoding.MaptilerResponse;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.repository.AccidentReportRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccidentReportService {
	private final AccidentReportRepository accidentReportRepository;
	private final UserRepository userRepository;
	private final IncidentService incidentService;
	private final GeocodingService geocodingService;

	public List<AccidentReportResponse> getAll(){
		List<AccidentReportResponse> accidentReportResponses = new ArrayList<>();
		for (AccidentReport accidentReport:accidentReportRepository.findAll()) {
			accidentReportResponses.add(
					AccidentReportResponse.of(accidentReport)
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
					AccidentReportResponse.of(accidentReport)
			);
		}
		return accidentReportResponses;
	}

	public AccidentReportResponse getById(Integer id){
		Optional<AccidentReport> accidentReportOptional = accidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Accident report with id %s not found", id));
		AccidentReport accidentReport = accidentReportOptional.get();

		return AccidentReportResponse.of(accidentReport);
	}
	
	public void add(AccidentReportRequest accidentReportRequest) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User reporter = null;
		if (!principal.equals("anonymousUser")) {
			UserPrincipal loggedPrincipal = (UserPrincipal) principal;
			reporter = userRepository.getByEmail(loggedPrincipal.getUsername());
		}

		MaptilerResponse geoResponse = geocodingService.getAddressFromCoordinates(Location.of(accidentReportRequest.getLongitude(), accidentReportRequest.getLatitude()));

		AccidentReport accidentReport = AccidentReport
				.builder()
				.reporter(reporter)
				.bandCode(accidentReportRequest.getBandCode())
				.breathing(accidentReportRequest.getBreathing())
				.conscious(accidentReportRequest.getConcious())
				.address(geoResponse.getFeatures().size() > 0 ? geoResponse.getFeatures().get(0).getPlaceName() : "UNKNOWN")
				.date(LocalDateTime.now())
        		.description(accidentReportRequest.getDescription())
				.emergencyType(accidentReportRequest.getEmergencyType())
				.victimCount(accidentReportRequest.getVictimCount())
				.location(Location.of(accidentReportRequest.getLongitude(), accidentReportRequest.getLatitude()))
				.build();

		accidentReportRepository.save(accidentReport);

		incidentService.addFromReport(accidentReport);

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
