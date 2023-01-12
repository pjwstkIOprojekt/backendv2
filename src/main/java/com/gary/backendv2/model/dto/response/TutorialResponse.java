package com.gary.backendv2.model.dto.response;

import com.gary.backendv2.model.Review;
import com.gary.backendv2.model.Tutorial;
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

    public static TutorialResponse of(Tutorial tutorial) {
        return TutorialResponse
                .builder()
                .tutorialId(tutorial.getTutorialId())
                .name(tutorial.getName())
                .tutorialType(tutorial.getTutorialType())
                .averageRating(tutorial.getReviewSet().stream().mapToDouble(Review::getValue).average().orElse(0))
                .thumbnail(tutorial.getThumbnail())
                .build();
    }
}
