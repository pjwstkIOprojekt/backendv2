package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Tutorial;
import com.gary.backendv2.model.users.User;
import lombok.Builder;

@Builder
public class ReviewResponse {

    Tutorial tutorial;
    User reviewer;
    Double value;
    String reviewDescription;
}