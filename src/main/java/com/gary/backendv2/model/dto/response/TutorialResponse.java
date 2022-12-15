package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.enums.TutorialType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TutorialResponse {

    Integer tutorialId;

    String name;

    String thumbnail;

    TutorialType tutorialType;

    Double averageRating;

    String tutorialHTML;
}
