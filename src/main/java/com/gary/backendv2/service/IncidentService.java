package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.AccidentReport;
import com.gary.backendv2.model.Ambulance;
import com.gary.backendv2.model.Incident;
import com.gary.backendv2.model.dto.request.IncidentRequest;
import com.gary.backendv2.model.dto.response.AccidentReportResponse;
import com.gary.backendv2.model.dto.response.IncidentResponse;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.repository.AccidentReportRepository;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IncidentService {
	private final IncidentRepository incidentRepository;
	private final AccidentReportRepository accidentReportRepository;
	private final AmbulanceRepository ambulanceRepository;
	private final AmbulanceService ambulanceService;

	public List<IncidentResponse> getAll(){
		List<IncidentResponse> incidentResponses = new ArrayList<>();
		List<Incident> incidents = incidentRepository.findAll();

		Map<Integer, Incident> incidentMap = populateIncidentMap(incidents);

		for (Incident incident : incidents) {
			incidentResponses.add(
				IncidentResponse
						.builder()
						.incidentId(incident.getIncidentId())
						.incidentStatusType(incident.getIncidentStatusType())
						.dangerScale(incident.getDangerScale())
						.reactionJustification(incident.getReactionJustification())
						.accidentReport(AccidentReportResponse.of(incidentMap.get(incident.getIncidentId()).getAccidentReport()))
						.build()
			);
		}
		return incidentResponses;
	}

	public IncidentResponse getById(Integer id){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		Incident incident = accidentReportOptional.get();

		return IncidentResponse
				.builder()
				.incidentId(incident.getIncidentId())
				.incidentStatusType(incident.getIncidentStatusType())
				.dangerScale(incident.getDangerScale())
				.reactionJustification(incident.getReactionJustification())
				.accidentReport(AccidentReportResponse.of(incident.getAccidentReport()))
				.build();
	}

	public void addFromReport(AccidentReport accidentReport){
		Incident incident = Incident
				.builder()
				.accidentReport(accidentReport)
				.incidentStatusType(IncidentStatusType.NEW)
				.build();
		incidentRepository.save(incident);
		accidentReport.setIncident(incident);
		accidentReportRepository.save(accidentReport);
	}

	public void update (Integer id, IncidentRequest incidentRequest){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		Incident incident = accidentReportOptional.get();

		incident.setIncidentStatusType(incidentRequest.getIncidentStatusType());
		incident.setDangerScale(incidentRequest.getDangerScale());
		incident.setReactionJustification(incidentRequest.getReactionJustification());
		incidentRepository.save(incident);
	}

	public void delete(Integer id){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		incidentRepository.delete(accidentReportOptional.get());
	}

	public void addAmbulances(Integer id, List<String> ambulancesLicencePlates){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		Incident incident = accidentReportOptional.get();
		List<Ambulance> selectedAmbulances = ambulanceRepository.getAmbulancesByLicensePlateIsIn(ambulancesLicencePlates);

		for (Ambulance a:selectedAmbulances) {
			a.getIncidents().add(incident);
			incident.getAmbulances().add(a);
			ambulanceService.changeAmbulanceState(a.getLicensePlate(), AmbulanceStateType.ON_ACTION);
			ambulanceRepository.save(a);
		}
		incidentRepository.save(incident);
	}

	private Map<Integer, Incident> populateIncidentMap(List<Incident> incidents) {
		Map<Integer, Incident> map = new HashMap<>();

		for (Incident incident : incidents) {
			map.put(incident.getIncidentId(), incident);
		}

		return map;
	}
}
