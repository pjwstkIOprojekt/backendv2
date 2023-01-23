package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.factories.ItemResponseFactoryProvider;
import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.model.*;
import com.gary.backendv2.model.ambulance.*;
import com.gary.backendv2.model.dto.PathElement;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.PostAmbulanceLocationRequest;
import com.gary.backendv2.model.dto.response.*;
import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.dto.response.users.MedicResponse;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.inventory.Inventory;
import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.model.users.employees.Medic;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.utils.ItemUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmbulanceService {
    private final EmployeeShiftRepository employeeShiftRepository;
    private final IncidentRepository incidentRepository;
    private final ItemService itemService;

    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceStateRepository ambulanceStateRepository;
    private final AmbulanceHistoryRepository ambulanceHistoryRepository;
    private final AmbulanceLocationRepository ambulanceLocationRepository;
    private final ItemRepository itemRepository;
    private final ItemContainerRepository itemContainerRepository;
    private final InventoryRepository inventoryRepository;
    private final CrewRepository crewRepository;
    private final MedicRepository medicRepository;
    private final AmbulanceIncidentHistoryRepository incidentHistoryRepository;

    public List<AmbulanceResponse> getAllAmbulances() {
        List<Ambulance> all = ambulanceRepository.findAll();
        List<AmbulanceResponse> ambulanceResponseList = new ArrayList<>();
        for (var ambulance : all) {
            ambulanceResponseList.add(AmbulanceResponse.of(ambulance));
        }

        return ambulanceResponseList;
    }

    public AmbulanceResponse getAmbulanceByLicensePlate(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        return AmbulanceResponse.of(ambulance);
    }

    public AmbulanceHistoryResponse getAmbulanceHistory(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        AmbulanceHistoryResponse ambulanceHistoryResponse = new AmbulanceHistoryResponse();
        Set<AmbulanceStateResponse> ambulanceStateResponseSet = populateStateResponseSet(ambulance);

        ambulanceHistoryResponse.setLicensePlate(ambulance.getLicensePlate());
        ambulanceHistoryResponse.setAmbulanceHistory(ambulanceStateResponseSet);

        return ambulanceHistoryResponse;
    }

    public AmbulanceStateResponse getAmbulanceCurrentState(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        ambulance.findCurrentState();
        AmbulanceState currentState = ambulance.getCurrentState();

        AmbulanceStateResponse ambulanceStateResponse = new AmbulanceStateResponse();
        ambulanceStateResponse.setType(currentState.getStateType());
        ambulanceStateResponse.setTimestamp(currentState.getTimestamp());

        return ambulanceStateResponse;
    }

    public Map<IncidentStatusType, List<IncidentResponse>> getAllIncidents(String licensePlate) {
        Map<IncidentStatusType, List<IncidentResponse>> responseMap = new HashMap<>() {{
            put(IncidentStatusType.NEW, new ArrayList<>());
            put(IncidentStatusType.CLOSED, new ArrayList<>());
            put(IncidentStatusType.ASSIGNED, new ArrayList<>());
            put(IncidentStatusType.ACCEPTED, new ArrayList<>());
            put(IncidentStatusType.REJECTED, new ArrayList<>());
        }};

        Ambulance ambulance = getAmbulance(licensePlate);

        AmbulanceIncidentHistory incidentHistory = ambulance.getIncidentHistory();
        for (var elem : incidentHistory.getIncidents()) {
            Incident incident = elem.getIncident();
            IncidentResponse response = IncidentResponse
                    .builder()
                    .incidentId(incident.getIncidentId())
                    .accidentReport(IncidentReportResponse.of(incident.getIncidentReport()))
                    .dangerScale(incident.getDangerScale())
                    .incidentStatusType(incident.getIncidentStatusType())
                    .reactionJustification(incident.getReactionJustification())
                    .build();

            responseMap.get(incident.getIncidentStatusType()).add(response);
        }

        return responseMap;
    }

    public IncidentResponse getCurrentIncident(String licensePlate) {
        Incident incident = getCurrentIncidentEntity(licensePlate);

        if (incident == null || incident.getIncidentStatusType() == IncidentStatusType.CLOSED) {
            throw new HttpException(HttpStatus.NO_CONTENT, "This ambulance is not assigned to any incidents");
        }

        return IncidentResponse
                .builder()
                .incidentId(incident.getIncidentId())
                .accidentReport(IncidentReportResponse.of(incident.getIncidentReport()))
                .dangerScale(incident.getDangerScale())
                .incidentStatusType(incident.getIncidentStatusType())
                .reactionJustification(incident.getReactionJustification())
                .build();
    }

    private Incident getCurrentIncidentEntity(String licensePlate) {
        List<Incident> incidents = incidentRepository.findAll();
        List<Incident> incidentCandidates = new ArrayList<>();

        for (Incident incident : incidents) {
            Optional<Ambulance> ambulanceOptional = incident.getAmbulances().stream().filter(x -> x.getLicensePlate().equals(licensePlate)).findFirst();
            if (ambulanceOptional.isEmpty()) {
                continue;
            }

            incidentCandidates.add(incident);
        }
        Optional<Incident> incidentOptional = incidentCandidates.stream().max(Comparator.comparing(Incident::getCreatedAt));

        return incidentOptional.orElse(null);
    }

    public List<MedicResponse> getCrewMedics(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        Crew crew = ambulance.getCrew();
        if (crew == null) {
            return Collections.emptyList();
        }
        Set<Medic> medics = crew.getMedics();

        List<MedicResponse> medicResponses = new ArrayList<>();
        medics.forEach(x -> {
            MedicResponse medicResponse = new MedicResponse();
            medicResponse.setEmail(x.getEmail());
            medicResponse.setFirstName(x.getFirstName());
            medicResponse.setLastName(x.getLastName());
            medicResponse.setUserId(x.getUserId());

            medicResponses.add(medicResponse);
        });

        return medicResponses;
    }

    public void removeMedics(String licensePlate, List<Integer> medicIds) {
        Ambulance ambulance = getAmbulance(licensePlate);

        List<Medic> medics = medicRepository.getAllByUserIdIn(medicIds);
        if (medics.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.format(", User with ids of: %s are not medics", medicIds));
        }

        Crew crew = null;
        if (ambulance.getCrew() == null) {
            crew = crewRepository.save(new Crew());
            ambulance.setCrew(crew);
        }

        crew = ambulance.getCrew();

        crew.getMedics().removeAll(medics);

        crewRepository.save(crew);
    }

    public void assignMedics(String licensePlate, List<Integer> medicIds) {
        Ambulance ambulance = getAmbulance(licensePlate);

        List<Medic> medics = medicRepository.getAllByUserIdIn(medicIds);
        if (medics.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, String.format(", User with ids of: %s are not medics", medicIds));
        }

        Crew crew = null;
        if (ambulance.getCrew() == null) {
            crew = crewRepository.save(new Crew());
            ambulance.setCrew(crew);
        }

        crew = ambulance.getCrew();

        crew.getMedics().addAll(medics);

        crewRepository.save(crew);
    }

    public AmbulanceResponse addAmbulance(AddAmbulanceRequest addRequest) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(addRequest.getLicensePlate());
        if (ambulanceOptional.isPresent()) throw new HttpException(HttpStatus.CONFLICT, String.format("Ambulance %s already exists", addRequest.getLicensePlate()));

        Ambulance ambulance = ambulanceRepository.save(createAmbulance(addRequest));

        return AmbulanceResponse.of(ambulance);
    }

    public AmbulanceStateResponse changeAmbulanceState(String licensePlate, AmbulanceStateType newState) {
        Ambulance ambulance = getAmbulance(licensePlate);

        AmbulanceState currentState =  ambulance.getCurrentState();
        if (currentState.getStateType() == newState) {
            throw new HttpException(HttpStatus.NO_CONTENT);
        }

        AmbulanceHistory ambulanceHistory = ambulance.getAmbulanceHistory();

        AmbulanceState state = new AmbulanceState();
        state.setStateType(newState);
        state.setTimestamp(LocalDateTime.now());

        state = ambulanceStateRepository.save(state);

        ambulance.setCurrentState(state);
        ambulanceRepository.save(ambulance);

        ambulanceHistory.getAmbulanceStates().add(state);
        ambulanceHistoryRepository.save(ambulanceHistory);

        AmbulanceStateResponse stateResponse = new AmbulanceStateResponse();
        stateResponse.setType(state.getStateType());
        stateResponse.setTimestamp(state.getTimestamp());
        return  stateResponse;
    }

    public void updateAmbulance(AddAmbulanceRequest addAmbulanceRequest) {
        Ambulance ambulance = getAmbulance(addAmbulanceRequest.getLicensePlate());

        ambulance.setLicensePlate(addAmbulanceRequest.getLicensePlate());
        ambulance.setAmbulanceType(addAmbulanceRequest.getAmbulanceType());
        ambulance.setAmbulanceClass(addAmbulanceRequest.getAmbulanceClass());
        ambulance.setSeats(addAmbulanceRequest.getSeats());

        ambulanceRepository.save(ambulance);
    }

    public void deleteAmbulance(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        ambulanceRepository.delete(ambulance);
    }

    public void addGeoLocation(String licensePlate, PostAmbulanceLocationRequest newLocation) {
        Ambulance ambulance = getAmbulance(licensePlate);
        Incident incident = getCurrentIncidentEntity(licensePlate);
        if (incident == null) {
            return;
        }


        if (ambulance.getCurrentState().getStateType() == AmbulanceStateType.ON_ACTION) {
            EnumSet<IncidentStatusType> allowedIncidentStatuses = EnumSet.of(IncidentStatusType.ASSIGNED, IncidentStatusType.ACCEPTED, IncidentStatusType.NEW);
            if (allowedIncidentStatuses.contains(incident.getIncidentStatusType())) {
                AmbulanceLocation ambulanceLocation = new AmbulanceLocation();
                ambulanceLocation.setAmbulance(ambulance);
                ambulanceLocation.setLocation(Location.of(newLocation.getLatitude(), newLocation.getLongitude()));
                ambulanceLocation.setTimestamp(LocalDateTime.now());
                ambulanceLocation.setIncident(incident.getIncidentId());

                ambulance.setLocation(Location.of(newLocation.getLatitude(), newLocation.getLongitude()));

                ambulanceLocationRepository.save(ambulanceLocation);
            }
        }
    }

    public void addItem(String licensePlate, Integer itemId) {
        Ambulance ambulance = getAmbulance(licensePlate);

        Inventory inventory = ambulance.getInventory();

        Map<Integer, ItemContainer> itemsInInventory = inventory.getItems();
        Item item = itemService.getById(itemId);

        ItemContainer container;
        if (itemsInInventory.containsKey(item.getItemId())) {
            container = itemsInInventory.get(item.getItemId());
            container.incrementCount();
        } else {
            ItemUtils itemUtils = ItemUtils.getInstance();

            container = new ItemContainer();
            container.setUnit(itemUtils.itemMeasuringUnitsLookup.get(item.getClass()));
            container.incrementCount();

            itemsInInventory.put(item.getItemId(), container);
        }

        itemContainerRepository.save(container);
        inventoryRepository.save(inventory);
    }

    public void addItems(String licensePlate, Integer itemId, Integer count) {
        Ambulance ambulance = getAmbulance(licensePlate);

        Inventory inventory = ambulance.getInventory();

        Map<Integer, ItemContainer> itemsInInventory = inventory.getItems();
        Item item = itemService.getById(itemId);

        ItemContainer container;
        if (itemsInInventory.containsKey(item.getItemId())) {
            container = itemsInInventory.get(item.getItemId());
            container.addMultiple(count);
        } else {
            ItemUtils itemUtils = ItemUtils.getInstance();

            container = new ItemContainer();
            container.setUnit(itemUtils.itemMeasuringUnitsLookup.get(item.getClass()));
            container.addMultiple(count);

            itemsInInventory.put(item.getItemId(), container);
        }

        itemContainerRepository.save(container);
        inventoryRepository.save(inventory);
    }

    public void editItemUnit(String licensePlate, Integer itemId, ItemContainer.Unit unit) {
        Ambulance ambulance = getAmbulance(licensePlate);

        Inventory inventory = ambulance.getInventory();
        inventory.getItems().get(itemId).setUnit(unit);

        inventoryRepository.save(inventory);
    }

    public List<EquipmentResponse> getItems(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        List<EquipmentResponse> responseList = new ArrayList<>();

        var itemsMap = ambulance.getInventory().getItems();
        List<Integer> itemIds = new ArrayList<>(itemsMap.keySet());

        List<Item> items = itemRepository.getItemsByItemIdIn(itemIds);
        if (!items.isEmpty()) {
            for (Item item : items) {
                ItemResponseAbstractFactory responseFactory = ItemResponseFactoryProvider.getItemFactory(item.getDiscriminatorValue());
                AbstractItemResponse itemResponse = responseFactory.createResponse(item);

                EquipmentResponse equipmentResponse = new EquipmentResponse();
                equipmentResponse.setItem(itemResponse);
                equipmentResponse.setItemData(new EquipmentResponse.ItemContainerResponse(itemsMap.get(item.getItemId())));

                responseList.add(equipmentResponse);
            }

            return responseList;
        }

        return Collections.emptyList();
    }

    public void removeItemById(String licensePlate, Integer itemId, Integer count) {
        Ambulance ambulance = getAmbulance(licensePlate);

        Inventory inventory = ambulance.getInventory();
        ItemContainer container = inventory.getItems().get(itemId);
        if (count != null && count > 1) {
            if (container.getCount() >= count) {
                container.removeMultiple(count);
            } else {
                throw new HttpException(HttpStatus.BAD_REQUEST, String.format("Requested to delete more items of id %s than there is (Item count -> %s > %s <- Requested count)", itemId, count, container.getCount()));
            }
        } else {
            container.decrementCount();
        }

        ambulanceRepository.save(ambulance);
    }

    public void removeAllItemById(String licensePlate, Integer itemId) {
        Ambulance ambulance = getAmbulance(licensePlate);

        Inventory inventory = ambulance.getInventory();
        ItemContainer container = inventory.getItems().get(itemId);

        container.removeMultiple(container.getCount());

        ambulanceRepository.save(ambulance);
    }

    public void clearInventory(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        Inventory inventory = ambulance.getInventory();

        for (Map.Entry<Integer, ItemContainer> kv : inventory.getItems().entrySet()) {
            ItemContainer container = kv.getValue();
            container.removeMultiple(kv.getValue().getCount());
        }

        ambulanceRepository.save(ambulance);
    }

    public AmbulancePathResponse getAmbulancePath(String licensePlate) {
        Ambulance ambulance = getAmbulance(licensePlate);

        List<AmbulanceLocation> ambulanceLocations = ambulanceLocationRepository.getAmbulanceLocationByAmbulance_LicensePlate(ambulance.getLicensePlate());

        Collections.sort(ambulanceLocations);

        List<PathElement> pathElements = new ArrayList<>();
        for (AmbulanceLocation p : ambulanceLocations) {
            pathElements.add(new PathElement( p.getTimestamp(), p.getLocation().getLongitude(), p.getLocation().getLatitude()));
        }

        return new AmbulancePathResponse(pathElements);
    }

    public AmbulancePathResponse getAmbulancePath(String licencePlate, Integer incidentId) {
        Ambulance ambulance = getAmbulance(licencePlate);
        List<AmbulanceLocation> ambulanceLocations = ambulanceLocationRepository.getAmbulanceLocationHistoryInIncident(ambulance.getAmbulanceId(), incidentId);

        List<PathElement> pathElements = new ArrayList<>();

        for (AmbulanceLocation al : ambulanceLocations) {
            pathElements.add(new PathElement(al.getTimestamp() ,al.getLocation().getLatitude(), al.getLocation().getLongitude()));
        }

        return new AmbulancePathResponse(pathElements, incidentId);
    }

    private Set<AmbulanceStateResponse> populateStateResponseSet(Ambulance ambulance) {
        Set<AmbulanceStateResponse> responseSet = new HashSet<>();

        AmbulanceHistory ambulanceHistory = ambulance.getAmbulanceHistory();

        for (AmbulanceState state : ambulanceHistory.getAmbulanceStates()) {
            AmbulanceStateResponse response = new AmbulanceStateResponse();
            response.setType(state.getStateType());
            response.setTimestamp(state.getTimestamp());

            responseSet.add(response);
        }

        return responseSet;
    }

    public Ambulance createAmbulance(AddAmbulanceRequest addRequest) {
        Inventory inventory = inventoryRepository.save(new Inventory());


        AmbulanceHistory ambulanceHistory = new AmbulanceHistory();
        AmbulanceIncidentHistory incidentHistory = incidentHistoryRepository.save(new AmbulanceIncidentHistory());

        AmbulanceState ambulanceState = new AmbulanceState();
        ambulanceState.setStateType(AmbulanceStateType.AVAILABLE);
        ambulanceState.setTimestamp(LocalDateTime.now());
        ambulanceState = ambulanceStateRepository.save(ambulanceState);

        ambulanceHistory.getAmbulanceStates().add(ambulanceState);
        ambulanceHistory = ambulanceHistoryRepository.save(ambulanceHistory);

        Crew crew = crewRepository.save(new Crew());

        Ambulance ambulance = new Ambulance();
        ambulance.setAmbulanceClass(addRequest.getAmbulanceClass());
        ambulance.setAmbulanceType(addRequest.getAmbulanceType());
        ambulance.setSeats(addRequest.getSeats());
        ambulance.setLicensePlate(addRequest.getLicensePlate());
        boolean locationAnyNull = Stream.of(addRequest.getLatitude(), addRequest.getLongitude()).anyMatch(Objects::isNull);
        ambulance.setLocation(locationAnyNull ? Location.defaultLocation() : Location.of(addRequest.getLongitude(), addRequest.getLatitude()));
        ambulance.setCurrentState(ambulanceState);
        ambulance.setAmbulanceHistory(ambulanceHistory);
        ambulance.setInventory(inventory);
        ambulance.setIncidentHistory(incidentHistory);
        ambulance.setCrew(crew);

        return ambulance;
    }

    private Ambulance getAmbulance(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));
        }

        return ambulanceOptional.get();
    }

}
