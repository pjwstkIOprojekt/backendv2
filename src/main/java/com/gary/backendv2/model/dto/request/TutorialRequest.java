package com.gary.backendv2.model.dto.request;

import com.gary.backendv2.model.enums.TutorialType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Data
public class TutorialRequest extends BaseRequest {
    Integer tutorialId;
    @NotBlank
    String name;
    TutorialType tutorialType;
    String tutorialHTML;
    String thumbnail;
}