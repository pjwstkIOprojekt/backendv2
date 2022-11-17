package com.gary.backendv2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class PathElement {
    LocalDateTime timestamp;
    Double longitude;
    Double latitude;
}
