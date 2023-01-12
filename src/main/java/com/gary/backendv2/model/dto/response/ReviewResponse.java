package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Tutorial;
import com.gary.backendv2.model.dto.response.users.GenericUserResponse;
import com.gary.backendv2.model.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewResponse {

    TutorialResponse tutorial;
    GenericUserResponse reviewer;
    Double value;
    String reviewDescription;
}