package com.gary.backendv2.service;

import com.gary.backendv2.AmbulanceIncidentHistoryElementRepository;
import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.VictimInfo;
import com.gary.backendv2.model.ambulance.AmbulanceIncidentHistoryElement;
import com.gary.backendv2.model.dto.request.VictimInfoRequest;
import com.gary.backendv2.model.dto.response.VictimInfoResponse;
import com.gary.backendv2.model.dto.response.users.MedicResponse;
import com.gary.backendv2.model.enums.EmergencyType;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.users.employees.Dispatcher;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.dto.request.IncidentRequest;
import com.gary.backendv2.model.dto.response.IncidentReportResponse;
import com.gary.backendv2.model.dto.response.IncidentResponse;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.repository.projections.IncidentInfo;
import com.gary.backendv2.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
	private final DispatcherRepository dispatcherRepository;
	private final VictimInfoRepository victimInfoRepository;

	private final AmbulanceService ambulanceService;
	private final AuthService authService;

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
				.ambulances(new HashSet<>())
				.dangerScale(calculateDangerScale(incidentReport))
				.incidentStatusType(IncidentStatusType.NEW)
				.createdAt(LocalDateTime.now())
				.build();
		assignDispatcher(incident);
		incidentRepository.save(incident);
		incidentReport.setIncident(incident);
		incidentReportRepository.save(incidentReport);
	}

	public void update (Integer id, IncidentRequest incidentRequest) {
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

	public VictimInfoResponse getVictimInfoById(Integer incidentId, Integer victimInfoId) {
		Incident incident = incidentRepository.findByIncidentId(incidentId).orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, String.format("Incident id %s not found", incidentId)));

		IncidentInfo incidentInfo = incidentRepository.getVictimsInfoByIncidentId(incidentId);


		VictimInfoResponse response = new VictimInfoResponse();
		for (IncidentInfo.VictimInfoInfo victim : incidentInfo.getVictims()) {
			if (victim.getVictimInfoId().equals(victimInfoId)) {
				MedicResponse medicResponse = new MedicResponse();
				medicResponse.setFirstName(victim.getMedic().getFirstName());
				medicResponse.setLastName(victim.getMedic().getLastName());
				medicResponse.setEmail(victim.getMedic().getEmail());
				medicResponse.setUserId(victim.getMedic().getUserId());

				response.setVictimInfoId(victim.getVictimInfoId());
				response.setGender(victim.getGender());
				response.setStatus(victim.getVictimStatus());
				response.setFirstName(victim.getFirstName());
				response.setLastName(victim.getLastName());
				response.setMedic(medicResponse);

				return response;

			}
		}

		throw new HttpException(HttpStatus.NO_CONTENT, "No victim info with given id " + victimInfoId);
	}

	public List<VictimInfoResponse> getVictimsInformation(Integer incidentId) {
		Optional<Incident> incidentOptional = incidentRepository.findByIncidentId(incidentId);
		if (incidentOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", incidentId));

		IncidentInfo incidentInfo = incidentRepository.getVictimsInfoByIncidentId(incidentId);

		List<VictimInfoResponse> responses = new ArrayList<>();
		for (IncidentInfo.VictimInfoInfo victim : incidentInfo.getVictims()) {
			MedicResponse medicResponse = new MedicResponse();
			medicResponse.setFirstName(victim.getMedic().getFirstName());
			medicResponse.setLastName(victim.getMedic().getLastName());
			medicResponse.setEmail(victim.getMedic().getEmail());
			medicResponse.setUserId(victim.getMedic().getUserId());

			VictimInfoResponse response = new VictimInfoResponse();
			response.setVictimInfoId(victim.getVictimInfoId());
			response.setFirstName(victim.getFirstName());
			response.setLastName(victim.getLastName());
			response.setGender(victim.getGender());
			response.setStatus(victim.getVictimStatus());
			response.setMedic(medicResponse);

			responses.add(response);
		}


		return responses;
	}

	public VictimInfoResponse updateVictimsInfo(Integer incidentId, Integer victimInfoId, VictimInfoRequest request, Authentication authentication) {
		Optional<Incident> incidentOptional = incidentRepository.findByIncidentId(incidentId);
		if (incidentOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", incidentId));

		Incident incident = incidentOptional.get();
		List<VictimInfo> victims = incident.getVictims();
		VictimInfo info = victims
				.stream()
				.filter(x -> x.getVictimInfoId().equals(victimInfoId))
				.findFirst()
				.orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Cannot find VictimInfo entity with given ID (" + victimInfoId +  ") in an Incident of ID: " + incidentId));

		info.setVictimStatus(request.getStatus());
		info.setFirstName(request.getFirstName());
		info.setLastName(request.getLastName());
		info.setGender(request.getGender());

		info = victimInfoRepository.save(info);

		MedicResponse medicResponse = new MedicResponse();
		medicResponse.setFirstName(info.getMedic().getFirstName());
		medicResponse.setLastName(info.getMedic().getLastName());
		medicResponse.setEmail(info.getMedic().getEmail());
		medicResponse.setUserId(info.getMedic().getUserId());

		VictimInfoResponse response = new VictimInfoResponse();
		response.setVictimInfoId(info.getVictimInfoId());
		response.setFirstName(info.getFirstName());
		response.setLastName(info.getLastName());
		response.setGender(info.getGender());
		response.setStatus(info.getVictimStatus());
		response.setMedic(medicResponse);

		return response;
	}

	public void addVictimInfo(Integer id, VictimInfoRequest request, Authentication authentication) {
		Optional<Incident> incidentOptional = incidentRepository.findByIncidentId(id);
		if (incidentOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Incident with id %s not found", id));

		User user = authService.getLoggedUserFromAuthentication(authentication);
		if (user == null) {
			throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting user information from the JWT token");
		}

		VictimInfo victimInfo = new VictimInfo();
		victimInfo.setFirstName(request.getFirstName());
		victimInfo.setLastName(request.getLastName());
		victimInfo.setVictimStatus(request.getStatus());
		victimInfo.setGender(request.getGender());
		victimInfo.setMedic(user);
		victimInfo = victimInfoRepository.save(victimInfo);

		Incident incident = incidentOptional.get();
		incident.getVictims().add(victimInfo);

		incidentRepository.save(incident);
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
					incident.getAmbulances().forEach(x -> {
						x.getIncidents().remove(incident);
						ambulanceService.changeAmbulanceState(x.getLicensePlate(), AmbulanceStateType.AVAILABLE);
					});
					incident.getDispatcher().setOpenIncidents(incident.getDispatcher().getOpenIncidents()-1);
				}
			}
			case ACCEPTED -> {
				if(incident.getIncidentStatusType() != IncidentStatusType.ASSIGNED){
					throw new HttpException(HttpStatus.BAD_REQUEST, "Can't set status type as" + IncidentStatusType.ACCEPTED.toString().toLowerCase());
				} else {
					incident.getAmbulances().forEach(x -> {
						x.getIncidents().remove(incident);
						ambulanceService.changeAmbulanceState(x.getLicensePlate(), AmbulanceStateType.AVAILABLE);
					});
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
		Map<EmergencyType, Double> dangerWeights = new HashMap<>() {{
			put(EmergencyType.FIRE, 3d);
			put(EmergencyType.COVID, 0.5);
			put(EmergencyType.SUICIDE, 0.77);
			put(EmergencyType.HEART_ATTACK, 5d);
			put(EmergencyType.UNKNOWN, 1d);
			put(EmergencyType.FLOOD, 1.33);
			put(EmergencyType.CAR_ACCIDENT, 1.2d);
		}};

		int breathingValue = incidentReport.isBreathing() ? 1 : 2;
		int consciousValue = incidentReport.isConscious() ? 1 : 2;
		int victimWeight = incidentReport.getVictimCount() == 0 ? 1 : incidentReport.getVictimCount();


		int dangerScale = (int) ((victimWeight * (1.33 * breathingValue + 1.55 * consciousValue)) * dangerWeights.get(incidentReport.getEmergencyType()));
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
