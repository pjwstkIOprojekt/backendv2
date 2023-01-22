package com.gary.backendv2.service;

import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.dto.response.geocoding.MaptilerResponse;
import com.gary.backendv2.utils.MaptilerConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class GeocodingService {
    private final MaptilerConstants maptiler;
    private final RestTemplate restTemplate = new RestTemplate();

    public MaptilerResponse getAddressFromCoordinates(Location location) {
        ResponseEntity<MaptilerResponse> response = restTemplate.getForEntity(maptiler.createGeoCodingURL(location.getLatitude(), location.getLongitude()), MaptilerResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Maptiler api response {}", Objects.requireNonNull(response.getBody()));
            throw new RuntimeException("Failed to call an external api");
        }

        // return response.getBody();

        var a = new MaptilerResponse();
        a.setType("test");
        a.setFeatures(new ArrayList<>());

        return a;
    }

}
