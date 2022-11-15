package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.PathElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AmbulancePathResponse {

    List<PathElement> path;
}