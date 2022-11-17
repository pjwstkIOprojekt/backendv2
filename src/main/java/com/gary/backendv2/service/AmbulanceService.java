package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.*;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.UpdateAmbulanceStateRequest;
import com.gary.backendv2.model.dto.response.AmbulanceHistoryResponse;
import com.gary.backendv2.model.dto.response.AmbulanceInventoryResponse;
import com.gary.backendv2.model.dto.response.AmbulanceResponse;
import com.gary.backendv2.model.dto.response.AmbulanceStateResponse;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.ItemCountUnit;
import com.gary.backendv2.model.enums.ItemType;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.utils.ItemFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmbulanceService {
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceStateRepository ambulanceStateRepository;
    private final AmbulanceHistoryRepository ambulanceHistoryRepository;

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
        ambulanceStateResponse.setTimeWindow(getStringLocalDateTimeMap(currentState));

        return ambulanceStateResponse;
    }

    public AmbulanceResponse addAmbulance(AddAmbulanceRequest addRequest) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(addRequest.getLicensePlate());
        if (ambulanceOptional.isPresent()) throw new HttpException(HttpStatus.CONFLICT, String.format("Ambulance %s already exists", addRequest.getLicensePlate()));

        Ambulance ambulance = ambulanceRepository.save(createAmbulance(addRequest));

        return AmbulanceResponse.of(ambulance);
    }

    public AmbulanceStateResponse changeAmbulanceState(String licensePlate, UpdateAmbulanceStateRequest newState) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) throw new HttpException(HttpStatus.NOT_FOUND, String.format("Ambulance %s not found", licensePlate));

        Ambulance ambulance = ambulanceOptional.get();
        AmbulanceHistory ambulanceHistory = ambulance.getAmbulanceHistory();

        AmbulanceState state = new AmbulanceState();
        state.setStateType(newState.getStateType());
        AmbulanceState.TimeWindow timeWindow = AmbulanceState.TimeWindow.fixed(newState.getStart(), newState.getEnd());
        state.setStateTimeWindow(timeWindow);

        state = ambulanceStateRepository.save(state);

        ambulance.setCurrentState(state);
        ambulanceRepository.save(ambulance);

        ambulanceHistory.getAmbulanceStates().add(state);
        ambulanceHistoryRepository.save(ambulanceHistory);

        AmbulanceStateResponse stateResponse = new AmbulanceStateResponse();
        stateResponse.setType(state.getStateType());
        stateResponse.setTimeWindow(getStringLocalDateTimeMap(state));
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

    public AmbulanceInventoryResponse getInventory(String licensePlate) {
        Optional<Ambulance> ambulanceOptional = ambulanceRepository.findByLicensePlate(licensePlate);
        if (ambulanceOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND);
        }
        Ambulance ambulance = ambulanceOptional.get();
        Inventory inventory = ambulance.getInventory();
        Map<ItemType, ItemGroup> inventoryItemMapping = inventory.getItemMapping();

        AmbulanceInventoryResponse inventoryResponse = new AmbulanceInventoryResponse();
        inventoryResponse.setLicensePlate(licensePlate);

        Map<ItemType, AmbulanceInventoryResponse.ItemGroupResponse> inv = new HashMap<>();
        for (var kv : inventoryItemMapping.entrySet()) {
            AmbulanceInventoryResponse.ItemGroupResponse groupResponse = new AmbulanceInventoryResponse.ItemGroupResponse();
            groupResponse.setItemsInGroup(kv.getValue().getItems().size());
            for (Item item : kv.getValue().getItems()) {
                AmbulanceInventoryResponse.ItemResponse itemResponse = new AmbulanceInventoryResponse.ItemResponse();

                switch (kv.getKey()) {
                    case CONSUMABLE -> {
                        ConsumableItem consumableItem = (ConsumableItem) item;

                        itemResponse.setAmount(consumableItem.getAmount());
                        itemResponse.setName(consumableItem.getName());
                        itemResponse.setUnit(consumableItem.getUnit());
                    }
                    case SINGLE_USE -> {
                        SingleUseItem singleUseItem = (SingleUseItem) item;

                        itemResponse.setName(singleUseItem.getName());
                        itemResponse.setAmount(1);
                        itemResponse.setUnit(singleUseItem.getUnit());
                    }
                }

                groupResponse.getItems().add(itemResponse);
            }

            inv.put(kv.getKey(), groupResponse);

        }
        inventoryResponse.setInventory(inv);

        return inventoryResponse;
    }

    private Set<AmbulanceStateResponse> populateStateResponseSet(Ambulance ambulance) {
        Set<AmbulanceStateResponse> responseSet = new HashSet<>();

        AmbulanceHistory ambulanceHistory = ambulance.getAmbulanceHistory();

        for (AmbulanceState state : ambulanceHistory.getAmbulanceStates()) {
            Map<String, LocalDateTime> timeMap = getStringLocalDateTimeMap(state);

            AmbulanceStateResponse response = new AmbulanceStateResponse();
            response.setType(state.getStateType());
            response.setTimeWindow(timeMap);

            responseSet.add(response);
        }

        return responseSet;
    }
    @Scheduled(fixedDelay = 60000 )
    public void checkForStateExpiration() {
        List<Ambulance> ambulances = ambulanceRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        for (Ambulance ambulance : ambulances) {
            AmbulanceState currentState = ambulance.findCurrentState();
            AmbulanceHistory history = ambulance.getAmbulanceHistory();

            if (currentState.getStateTimeWindow().getEndTime() != null && currentState.getStateTimeWindow().getEndTime().isBefore(now)) {
                AmbulanceState newState = new AmbulanceState();
                newState.setStateType(AmbulanceStateType.AVAILABLE);
                newState.setStateTimeWindow(AmbulanceState.TimeWindow.startingFrom(now));
                newState = ambulanceStateRepository.save(newState);

                history.getAmbulanceStates().add(newState);
                ambulanceHistoryRepository.save(history);
            }
        }
    }

    private Map<String, LocalDateTime> getStringLocalDateTimeMap(AmbulanceState state) {
        Map<String, LocalDateTime> timeMap = new HashMap<>();
        timeMap.put("start", state.getStateTimeWindow().getStartTime());
        // God, I hate languages that are not null safe...
        // it threw null pointer exception without this check
        if (state.getStateTimeWindow().getEndTime() == null) {
            timeMap.put("end", null);
        } else timeMap.put("end", state.getStateTimeWindow().getEndTime());
        return timeMap;
    }

    private Ambulance createAmbulance(AddAmbulanceRequest addRequest) {
        AmbulanceHistory ambulanceHistory = new AmbulanceHistory();
        AmbulanceState ambulanceState = new AmbulanceState();

        prepareDefaultAmbulanceState(ambulanceHistory, ambulanceState);

        Ambulance ambulance = new Ambulance();
        ambulance.setAmbulanceClass(addRequest.getAmbulanceClass());
        ambulance.setAmbulanceType(addRequest.getAmbulanceType());
        ambulance.setSeats(addRequest.getSeats());
        ambulance.setLicensePlate(addRequest.getLicensePlate());
        boolean locationAnyNull = Stream.of(addRequest.getLatitude(), addRequest.getLongitude()).anyMatch(Objects::isNull);
        ambulance.setLocation(locationAnyNull ? Location.defaultLocation() : Location.of(addRequest.getLongitude(), addRequest.getLatitude()));
        ambulance.setCurrentState(ambulanceState);
        ambulance.setAmbulanceHistory(ambulanceHistory);
        ambulance.setInventory(prepareInventoryForAmbulance());

        return ambulance;
    }

    private void prepareDefaultAmbulanceState(AmbulanceHistory ambulanceHistory, AmbulanceState ambulanceState) {
        ambulanceState.setStateType(AmbulanceStateType.AVAILABLE);
        ambulanceState.setStateTimeWindow(AmbulanceState.TimeWindow.startingFrom(LocalDateTime.now()));
        ambulanceState = ambulanceStateRepository.save(ambulanceState);

        ambulanceHistory.getAmbulanceStates().add(ambulanceState);
        ambulanceHistory = ambulanceHistoryRepository.save(ambulanceHistory);
    }

    private final ItemFactory itemFactory;
    private final ItemGroupRepository itemGroupRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    private Inventory prepareInventoryForAmbulance() {
        Inventory inventory = inventoryRepository.save(new Inventory());

        // create example items
        SingleUseItem bandage = (SingleUseItem) itemFactory.createItem(ItemType.SINGLE_USE);
        bandage.setName("Bandage");

        ConsumableItem painkillers = (ConsumableItem) itemFactory.createItem(ItemType.CONSUMABLE);
        painkillers.setName("Painkillers");
        painkillers.setAmount(20);
        painkillers.setUnit(ItemCountUnit.COUNT);

        ConsumableItem otherConsumable = (ConsumableItem) itemFactory.createItem(ItemType.CONSUMABLE);
        otherConsumable.setName("IDK no idea what else can be consumable (TM)");
        otherConsumable.setAmount(5);
        otherConsumable.setUnit(ItemCountUnit.PACK);

        bandage = itemRepository.save(bandage);
        painkillers = itemRepository.save(painkillers);
        otherConsumable = itemRepository.save(otherConsumable);

        // prepare item groups
        for (ItemType itemType : ItemType.values()) {
            ItemGroup itemGroup = new ItemGroup(inventory);
            switch (itemType) {
                case CONSUMABLE -> {
                    painkillers.setItemGroup(itemGroup);
                    otherConsumable.setItemGroup(itemGroup);

                    itemGroup.getItems().add(painkillers);
                    itemGroup.getItems().add(otherConsumable);
                }
                case SINGLE_USE -> {
                    bandage.setItemGroup(itemGroup);
                    itemGroup.getItems().add(bandage);
                }
            }

            itemGroup = itemGroupRepository.save(itemGroup);

            inventory.getItemMapping().put(itemType, itemGroup);
        }

        return inventoryRepository.save(inventory);

    }
}
