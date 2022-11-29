package com.gary.backendv2.model.dto.response.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class MaptilerResponse {
    @Getter
    @Setter
    @ToString
    public static class MapTilerFeature {
        String type;
        String id;
        @JsonProperty("place_name")
        String placeName;
        String text;
        int relevance;
    }

    String type;
    List<MapTilerFeature> features;
}
