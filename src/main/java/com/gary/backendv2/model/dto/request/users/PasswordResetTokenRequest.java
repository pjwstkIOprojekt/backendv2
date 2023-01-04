package com.gary.backendv2.model.dto.request.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class PasswordResetTokenRequest {
    @Email
    String email;
}
