package com.gary.backendv2.model.dto.response.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.gary.backendv2.model.dto.response.WorkScheduleResponse;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericUserResponse {
    private Integer id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String bandCode;
    private LocalDate birthDate;
    private EmployeeType employeeType;
    @JsonUnwrapped
    private WorkScheduleResponse workSchedule;

    public static GenericUserResponse of(User user) {
        GenericUserResponse r = new GenericUserResponse();
        r.setId(user.getUserId());
        r.setPhone(user.getPhoneNumber());
        r.setEmail(user.getEmail());
        r.setName(user.getFirstName());
        r.setLastName(user.getLastName());
        r.setBirthDate(user.getBirthDate());
        r.setBandCode(user.getBandCode());

        return r;
    }
}
