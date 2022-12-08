package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.TutorialType;
import lombok.Builder;

@Builder
public class AllTutorialsResponse {


    Integer tutorialId;

    String name;

    String thumbnail;

    TutorialType tutorialType;

    Double avarageRating;

}
