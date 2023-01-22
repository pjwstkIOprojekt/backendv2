package com.gary.backendv2.model.dto.response.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gary.backendv2.model.dto.response.IncidentResponse;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DispatcherResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Integer userId;

    private Set<IncidentResponse> assignedIncidents = new HashSet<>();
}
