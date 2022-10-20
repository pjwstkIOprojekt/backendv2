package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Ambulance;
import com.gary.backendv2.model.enums.AmbulanceClass;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.AmbulanceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmbulanceResponse {
    private String licensePlate;
    private AmbulanceClass ambulanceClass;
    private AmbulanceType ambulanceType;
    private AmbulanceStateType ambulanceStateType;

    public static AmbulanceResponse of(Ambulance ambulance) {
        AmbulanceResponse ambulanceResponse = new AmbulanceResponse();
        ambulanceResponse.setAmbulanceClass(ambulance.getAmbulanceClass());
        ambulanceResponse.setAmbulanceType(ambulance.getAmbulanceType());
        ambulanceResponse.setLicensePlate(ambulance.getLicensePlate());
        ambulanceResponse.setAmbulanceStateType(ambulance.getAmbulanceState().getStateType());

        return ambulanceResponse;
    }
}
