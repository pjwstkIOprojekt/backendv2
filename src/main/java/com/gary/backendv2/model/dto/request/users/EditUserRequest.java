package com.gary.backendv2.model.dto.request.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gary.backendv2.model.dto.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
public class EditUserRequest extends BaseRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    @NotNull
    private LocalDate birthDate;
}
