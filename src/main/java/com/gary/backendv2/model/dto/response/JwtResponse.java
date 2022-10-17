package com.gary.backendv2.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Builder
public class JwtResponse {
    Integer userId;
    String token;
    String email;
    String type;
    Collection<String> roles;
}
