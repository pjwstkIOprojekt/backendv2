package com.gary.backendv2.model.dto.request.users;

import com.gary.backendv2.model.dto.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordRequest extends BaseRequest {
    @NotBlank
    String newPassword;
}
