package com.gary.backendv2.model.dto.request.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank
    String oldPassword;
    @NotBlank
    String newPassword;
}
