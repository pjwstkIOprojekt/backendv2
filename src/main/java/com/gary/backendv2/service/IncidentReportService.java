package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.dto.request.*;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.response.IncidentReportResponse;
import com.gary.backendv2.model.dto.response.geocoding.MaptilerResponse;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.repository.IncidentReportRepository;
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
public class IncidentReportService {
	private final IncidentReportRepository incidentReportRepository;
	private final UserRepository userRepository;
	private final IncidentService incidentService;
	private final GeocodingService geocodingService;

	public List<IncidentReportResponse> getAll(){
		List<IncidentReportResponse> incidentReportRespons = new ArrayList<>();
		for (IncidentReport incidentReport : incidentReportRepository.findAll()) {
			incidentReportRespons.add(
					IncidentReportResponse.of(incidentReport)
			);
		}
		return incidentReportRespons;
	}

	public List<IncidentReportResponse> getAllByUser(String email){
		List<IncidentReportResponse> incidentReportRespons = new ArrayList<>();
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isEmpty()) {
			throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", email));
		}
		for (IncidentReport incidentReport : userOptional.get().getIncidentReports()) {
			incidentReportRespons.add(
					IncidentReportResponse.of(incidentReport)
			);
		}
		return incidentReportRespons;
	}

	public IncidentReportResponse getById(Integer id){
		Optional<IncidentReport> accidentReportOptional = incidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident report with id %s not found", id));
		IncidentReport incidentReport = accidentReportOptional.get();

		return IncidentReportResponse.of(incidentReport);
	}
	
	public void add(IncidentReportRequest incidentReportRequest) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User reporter = null;
		if (!principal.equals("anonymousUser")) {
			UserPrincipal loggedPrincipal = (UserPrincipal) principal;
			reporter = userRepository.getByEmail(loggedPrincipal.getUsername());
		}

		MaptilerResponse geoResponse = geocodingService.getAddressFromCoordinates(Location.of(incidentReportRequest.getLongitude(), incidentReportRequest.getLatitude()));

		IncidentReport incidentReport = IncidentReport
				.builder()
				.reporter(reporter)
				.bandCode(incidentReportRequest.getBandCode())
				.breathing(incidentReportRequest.getBreathing())
				.conscious(incidentReportRequest.getConcious())
				.address(geoResponse.getFeatures().size() > 0 ? geoResponse.getFeatures().get(0).getPlaceName() : "UNKNOWN")
				.date(LocalDateTime.now())
        		.description(incidentReportRequest.getDescription())
				.emergencyType(incidentReportRequest.getEmergencyType())
				.location(Location.of(incidentReportRequest.getLongitude(), incidentReportRequest.getLatitude()))
				.build();

		incidentReportRepository.save(incidentReport);

		incidentService.addFromReport(incidentReport);

	}

	public void updateById(Integer id, AccidentReportUpdateRequest accidentReportUpdateRequest){
		Optional<IncidentReport> accidentReportOptional = incidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident report with id %s not found", id));
		IncidentReport incidentReport = accidentReportOptional.get();
		incidentReport.setBreathing(accidentReportUpdateRequest.isBreathing());
		incidentReport.setDescription(accidentReportUpdateRequest.getDescription());
		incidentReport.setBandCode(accidentReportUpdateRequest.getBandCode());
		incidentReport.setConscious(accidentReportUpdateRequest.isConsciousness());
		incidentReport.setEmergencyType(accidentReportUpdateRequest.getEmergencyType());
		incidentReport.setLocation(Location.of(accidentReportUpdateRequest.getLongitude(), accidentReportUpdateRequest.getLatitude()));
		incidentReportRepository.save(incidentReport);
	}

	public void deleteById(Integer id){
		Optional<IncidentReport> accidentReportOptional = incidentReportRepository.findByAccidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident report with id %s not found", id));
		incidentReportRepository.delete(accidentReportOptional.get());
	}
}
