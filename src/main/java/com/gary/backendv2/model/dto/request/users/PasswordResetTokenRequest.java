package com.gary.backendv2.model.dto.request.users;

import com.gary.backendv2.model.dto.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class PasswordResetTokenRequest extends BaseRequest {
    @Email
    String email;
}
