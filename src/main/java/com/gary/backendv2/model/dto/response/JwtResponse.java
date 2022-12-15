package com.gary.backendv2.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Builder
public class JwtResponse {
    String token;
    String email;
    String type;
    Integer userId;
    Collection<String> roles;
}
