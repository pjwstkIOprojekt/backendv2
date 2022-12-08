package com.gary.backendv2.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.AmbulanceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmbulanceResponse {
    private String licensePlate;
    private AmbulanceClass ambulanceClass;
    private AmbulanceType ambulanceType;
    private AmbulanceStateType ambulanceStateType;
    private Integer ambulanceId;
    private Integer seats;
    private Location currentLocation;

    public static AmbulanceResponse of(Ambulance ambulance) {
        ambulance.setCurrentState(ambulance.getAmbulanceHistory().getAmbulanceStates().get(ambulance.getAmbulanceHistory().getAmbulanceStates().size() - 1));

        AmbulanceResponse ambulanceResponse = new AmbulanceResponse();
        ambulanceResponse.setAmbulanceClass(ambulance.getAmbulanceClass());
        ambulanceResponse.setAmbulanceType(ambulance.getAmbulanceType());
        ambulanceResponse.setLicensePlate(ambulance.getLicensePlate());
        ambulanceResponse.setAmbulanceStateType(ambulance.getCurrentState().getStateType());
        ambulanceResponse.setSeats(ambulance.getSeats());
        ambulanceResponse.setCurrentLocation(ambulance.getLocation());

        return ambulanceResponse;
    }
}
