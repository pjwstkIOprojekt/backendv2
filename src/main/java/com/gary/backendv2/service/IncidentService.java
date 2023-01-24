package com.gary.backendv2.service;

import com.gary.backendv2.AmbulanceIncidentHistoryElementRepository;
import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.ambulance.AmbulanceIncidentHistoryElement;
import com.gary.backendv2.model.ambulance.AmbulanceState;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.users.employees.Dispatcher;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.dto.request.IncidentRequest;
import com.gary.backendv2.model.dto.response.IncidentReportResponse;
import com.gary.backendv2.model.dto.response.IncidentResponse;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {
	private final WorkScheduleRepository workScheduleRepository;
	private final IncidentRepository incidentRepository;
	private final IncidentReportRepository incidentReportRepository;
	private final AmbulanceRepository ambulanceRepository;
	private final AmbulanceService ambulanceService;
	private final DispatcherRepository dispatcherRepository;

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
						.accidentReport(IncidentReportResponse.of(incidentMap.get(incident.getIncidentId()).getIncidentReport()))
						.build()
			);
		}
		return incidentResponses;
	}

	public IncidentResponse getById(Integer id) {
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		Incident incident = accidentReportOptional.get();

		return IncidentResponse
				.builder()
				.incidentId(incident.getIncidentId())
				.incidentStatusType(incident.getIncidentStatusType())
				.dangerScale(incident.getDangerScale())
				.reactionJustification(incident.getReactionJustification())
				.accidentReport(IncidentReportResponse.of(incident.getIncidentReport()))
				.build();
	}

	public List<IncidentResponse> getByStatus(IncidentStatusType statusType){
		List<IncidentResponse> incidentResponses = new ArrayList<>();
		List<Incident> incidents = incidentRepository.findAllByIncidentStatusType(statusType);

		Map<Integer, Incident> incidentMap = populateIncidentMap(incidents);

		for (Incident incident : incidents) {
			incidentResponses.add(
					IncidentResponse
							.builder()
							.incidentId(incident.getIncidentId())
							.incidentStatusType(incident.getIncidentStatusType())
							.dangerScale(incident.getDangerScale())
							.reactionJustification(incident.getReactionJustification())
							.accidentReport(IncidentReportResponse.of(incidentMap.get(incident.getIncidentId()).getIncidentReport()))
							.build()
			);
		}
		return incidentResponses;
	}

	public void addFromReport(IncidentReport incidentReport){
		Incident incident = Incident
				.builder()
				.incidentReport(incidentReport)
				.dangerScale(calculateDangerScale(incidentReport))
				.incidentStatusType(IncidentStatusType.NEW)
				.createdAt(LocalDateTime.now())
				.build();
		assignDispatcher(incident);
		incidentRepository.save(incident);
		incidentReport.setIncident(incident);
		incidentReportRepository.save(incidentReport);
	}

	public void update (Integer id, IncidentRequest incidentRequest){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		Incident incident = accidentReportOptional.get();

		if(incident.getIncidentStatusType() != incidentRequest.getIncidentStatusType()){
			changeIncidentStatus(id, incidentRequest.getIncidentStatusType());
		}
		incident.setDangerScale(incidentRequest.getDangerScale());
		incident.setReactionJustification(incidentRequest.getReactionJustification());
		incidentRepository.save(incident);
	}

	public void delete(Integer id){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		incidentRepository.delete(accidentReportOptional.get());
	}

	private final AmbulanceIncidentHistoryElementRepository elementRepository;
	public Map<String, String> addAmbulances(Integer id, List<String> ambulancesLicencePlates){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		Map<String, String> infoMap = new HashMap<>();

		Incident incident = accidentReportOptional.get();
		List<Ambulance> selectedAmbulances = ambulanceRepository.getAmbulancesByLicensePlateIsIn(ambulancesLicencePlates);

		for (Ambulance a : selectedAmbulances) {
			infoMap.put(a.getLicensePlate(), null);
			if (a.getCurrentState().getStateType() == AmbulanceStateType.AVAILABLE) {
				a.getIncidents().add(incident);
				AmbulanceIncidentHistoryElement historyElement = elementRepository.save(new AmbulanceIncidentHistoryElement());
				historyElement.setIncident(incident);
				historyElement.setUpdatedAt(LocalDateTime.now());
				a.getIncidentHistory().getIncidents().add(historyElement);
				incident.getAmbulances().add(a);

				ambulanceService.changeAmbulanceState(a.getLicensePlate(), AmbulanceStateType.ON_ACTION);
				ambulanceRepository.save(a);

				infoMap.replace(a.getLicensePlate(), String.format("Successfully assigned %s to incident of id %s", a.getLicensePlate(), id));
			} else {
				infoMap.replace(a.getLicensePlate(), String.format("Ambulance %s cannot be assigned to incident of id %s because Ambulance status is %s which is not %s", a.getLicensePlate(), id, a.getCurrentState().getStateType(), AmbulanceStateType.AVAILABLE));
			}
		}
		incidentRepository.save(incident);

		return infoMap;
	}

	@Transactional
	public void changeIncidentStatus(Integer id, IncidentStatusType incidentStatusType){
		Optional<Incident> accidentReportOptional = incidentRepository.findByIncidentId(id);
		if (accidentReportOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));
		Incident incident = accidentReportOptional.get();
		switch (incidentStatusType){
			case NEW -> throw new HttpException(HttpStatus.BAD_REQUEST, "Can't set status type as" + IncidentStatusType.NEW.toString().toLowerCase());
			case CLOSED -> {
				incident.getDispatcher().setOpenIncidents(incident.getDispatcher().getOpenIncidents()-1);
				incident.getAmbulances().forEach(x -> {
					x.getIncidents().remove(incident);
					ambulanceService.changeAmbulanceState(x.getLicensePlate(), AmbulanceStateType.AVAILABLE);
				});
			}
			case ASSIGNED -> {
				if (incident.getIncidentStatusType() != IncidentStatusType.NEW){
					throw new HttpException(HttpStatus.BAD_REQUEST, "Can't set status type as" + IncidentStatusType.ASSIGNED.toString().toLowerCase());
				}
			}
			case REJECTED -> {
				if(incident.getIncidentStatusType() != IncidentStatusType.ASSIGNED){
					throw new HttpException(HttpStatus.BAD_REQUEST, "Can't set status type as" + IncidentStatusType.REJECTED.toString().toLowerCase());
				}else{
					incident.getDispatcher().setOpenIncidents(incident.getDispatcher().getOpenIncidents()-1);
				}
			}
			case ACCEPTED -> {
				if(incident.getIncidentStatusType() != IncidentStatusType.ASSIGNED){
					throw new HttpException(HttpStatus.BAD_REQUEST, "Can't set status type as" + IncidentStatusType.ACCEPTED.toString().toLowerCase());
				}
			}
		}
		incident.setIncidentStatusType(incidentStatusType);
		dispatcherRepository.save(incident.getDispatcher());
		incidentRepository.save(incident);
	}

	private Map<Integer, Incident> populateIncidentMap(List<Incident> incidents) {
		Map<Integer, Incident> map = new HashMap<>();

		for (Incident incident : incidents) {
			map.put(incident.getIncidentId(), incident);
		}

		return map;
	}


	private void assignDispatcher(Incident incident) {
		List<Dispatcher> dispatchers = getAllByWorking();

		if(dispatchers.size() == 0) {
			throw new HttpException(HttpStatus.NOT_ACCEPTABLE, "No dispatchers currently available");
		}
		List<Dispatcher> possibleAssigments = new ArrayList<>();
		int activeIncidents = Integer.MAX_VALUE;

		for (Dispatcher dispatcher : dispatchers) {
			if(dispatcher.getOpenIncidents() < activeIncidents){
				possibleAssigments.clear();
				activeIncidents = dispatcher.getOpenIncidents();
			}
			if(dispatcher.getOpenIncidents() == activeIncidents){
				possibleAssigments.add(dispatcher);
			}
		}
		Dispatcher dispatcher;
		if (possibleAssigments.size() == 1) {
			dispatcher = possibleAssigments.get(0);
		} else {
			dispatcher = possibleAssigments.get(ThreadLocalRandom.current().nextInt(0, possibleAssigments.size()-1));
		}

		dispatcher.setOpenIncidents(dispatcher.getOpenIncidents()+1);
		dispatcher.getIncidents().add(incident);
		incident.setDispatcher(dispatcher);
		incident = incidentRepository.save(incident);
		changeIncidentStatus(incident.getIncidentId(), IncidentStatusType.ASSIGNED);
		dispatcherRepository.save(dispatcher);
	}

	private int calculateDangerScale(IncidentReport incidentReport) {
		int breathingValue = incidentReport.isBreathing() ? 1 : 0;
		int consciousValue = incidentReport.isConscious() ? 1 : 0;

		int dangerScale = (int) (incidentReport.getVictimCount() * (0.75 * breathingValue + 0.55 * consciousValue));
		if (dangerScale > 10) {
			dangerScale = 10;
		}

		return Math.max(dangerScale, 1);
	}

	private List<Dispatcher> getAllByWorking(){
		List<Dispatcher> dispatchers = new ArrayList<>();

		for(Dispatcher d: dispatcherRepository.findAll()){
			if(d.getWorking()) dispatchers.add(d);
		}
		return dispatchers;
	}
}
