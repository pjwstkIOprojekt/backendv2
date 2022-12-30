package com.gary.backendv2.model.dto.response.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Integer userId;
}
