package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.factories.ItemResponseFactoryProvider;
import com.gary.backendv2.factories.asbtract.ItemResponseAbstractFactory;
import com.gary.backendv2.model.*;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.ambulance.AmbulanceHistory;
import com.gary.backendv2.model.ambulance.AmbulanceLocation;
import com.gary.backendv2.model.ambulance.AmbulanceState;
import com.gary.backendv2.model.dto.PathElement;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.PostAmbulanceLocationRequest;
import com.gary.backendv2.model.dto.response.*;
import com.gary.backendv2.model.dto.response.items.AbstractItemResponse;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.inventory.Inventory;
import com.gary.backendv2.model.inventory.ItemContainer;
import com.gary.backendv2.model.inventory.items.Item;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.utils.ItemUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmbulanceService {
    private final ItemService itemService;

    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceStateRepository ambulanceStateRepository;
    private final AmbulanceHistoryRepository ambulanceHistoryRepository;
    private final AmbulanceLocationRepository ambulanceLocationRepository;
    private final ItemRepository itemRepository;
    private final ItemContainerRepository itemContainerRepository;
    private final InventoryRepository inventoryRepository;

    public List<AmbulanceResponse> getAllAmbulances() {
        List<Ambulance> all = ambulanceRepository.findAll();
        List<AmbulanceResponse> ambulanceResponseList = new ArrayList<>();
        for (var ambulance : all) {
            ambulanceResponseList.add(AmbulanceResponse.of(ambulance));
        }

        return ambulanceResponseList;
    }

    public AmbulanceResponse getAmbulanceByLicensePlate(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));
        else return AmbulanceResponse.of(ambulanceOptional.get());
    }

    public AmbulanceHistoryResponse getAmbulanceHistory(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        AmbulanceHistoryResponse ambulanceHistoryResponse = new AmbulanceHistoryResponse();
        Ambulance ambulance = ambulanceOptional.get();
        Set<AmbulanceStateResponse> ambulanceStateResponseSet = populateStateResponseSet(ambulance);

        ambulanceHistoryResponse.setLicensePlate(ambulance.getLicensePlate());
        ambulanceHistoryResponse.setAmbulanceHistory(ambulanceStateResponseSet);

        return ambulanceHistoryResponse;
    }

    public AmbulanceStateResponse getAmbulanceCurrentState(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        Ambulance ambulance = ambulanceOptional.get();
        ambulance.findCurrentState();
        AmbulanceState currentState = ambulance.getCurrentState();

        AmbulanceStateResponse ambulanceStateResponse = new AmbulanceStateResponse();
        ambulanceStateResponse.setType(currentState.getStateType());
        ambulanceStateResponse.setTimestamp(currentState.getTimestamp());

        return ambulanceStateResponse;
    }

    public AmbulanceResponse addAmbulance(AddAmbulanceRequest addRequest) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(addRequest.getLicensePlate());
        if (ambulanceOptional.isPresent()) throw new HttpException(HttpStatus.CONFLICT, String.format("Ambulance %s already exists", addRequest.getLicensePlate()));

        Ambulance ambulance = ambulanceRepository.save(createAmbulance(addRequest));

        return AmbulanceResponse.of(ambulance);
    }

    public AmbulanceStateResponse changeAmbulanceState(String licensePlate, AmbulanceStateType newState) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        Ambulance ambulance = ambulanceOptional.get();

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
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(addAmbulanceRequest.getLicensePlate());
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", addAmbulanceRequest.getLicensePlate()));

        Ambulance ambulance = ambulanceOptional.get();

        ambulance.setLicensePlate(addAmbulanceRequest.getLicensePlate());
        ambulance.setAmbulanceType(addAmbulanceRequest.getAmbulanceType());
        ambulance.setAmbulanceClass(addAmbulanceRequest.getAmbulanceClass());
        ambulance.setSeats(addAmbulanceRequest.getSeats());

        ambulanceRepository.save(ambulance);
    }

    public void deleteAmbulance(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        ambulanceRepository.delete(ambulanceOptional.get());
    }

    public void addGeoLocation(String licensePlate, PostAmbulanceLocationRequest newLocation) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }
        Ambulance ambulance = ambulanceOptional.get();

        if (ambulance.getCurrentState().getStateType() == AmbulanceStateType.ON_ACTION) {
            AmbulanceLocation ambulanceLocation = new AmbulanceLocation();
            ambulanceLocation.setAmbulance(ambulance);
            ambulanceLocation.setLocation(Location.of(newLocation.getLongitude(), newLocation.getLatitude()));
            ambulanceLocation.setTimestamp(LocalDateTime.now());

            ambulanceLocationRepository.save(ambulanceLocation);
        }
    }

    public void addItem(String licensePlate, Integer itemId) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }

        Ambulance ambulance = ambulanceOptional.get();
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
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }

        Ambulance ambulance = ambulanceOptional.get();
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

    public List<EquipmentResponse> getItems(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }

        List<EquipmentResponse> responseList = new ArrayList<>();

        Ambulance ambulance = ambulanceOptional.get();
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

    public AmbulancePathResponse getAmbulancePath(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }
        Ambulance ambulance = ambulanceOptional.get();

        List<AmbulanceLocation> ambulanceLocations = ambulanceLocationRepository.getAmbulanceLocationByAmbulance_LicensePlate(ambulance.getLicensePlate());

        Collections.sort(ambulanceLocations);

        List<PathElement> pathElements = new ArrayList<>();
        for (AmbulanceLocation p : ambulanceLocations) {
            pathElements.add(new PathElement( p.getTimestamp(), p.getLocation().getLongitude(), p.getLocation().getLatitude()));
        }

        return new AmbulancePathResponse(pathElements);
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

    private Ambulance createAmbulance(AddAmbulanceRequest addRequest) {
        Inventory inventory = inventoryRepository.save(new Inventory());


        AmbulanceHistory ambulanceHistory = new AmbulanceHistory();

        AmbulanceState ambulanceState = new AmbulanceState();
        ambulanceState.setStateType(AmbulanceStateType.AVAILABLE);
        ambulanceState.setTimestamp(LocalDateTime.now());
        ambulanceState = ambulanceStateRepository.save(ambulanceState);

        ambulanceHistory.getAmbulanceStates().add(ambulanceState);
        ambulanceHistory = ambulanceHistoryRepository.save(ambulanceHistory);

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

        return ambulance;
    }

}
