package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.dto.response.users.MedicResponse;
import com.gary.backendv2.model.enums.Gender;
import com.gary.backendv2.model.enums.VictimStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VictimInfoResponse {
    private Integer victimInfoId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private VictimStatus status;

    private MedicResponse medic;
}
