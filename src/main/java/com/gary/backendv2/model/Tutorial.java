package com.gary.backendv2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gary.backendv2.model.enums.TutorialType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "tutorial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tutorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tutorial_id")
    private Integer tutorialId;

    private String name;

    @Enumerated(EnumType.STRING)
    private TutorialType tutorialType;

    @Column(columnDefinition = "text")
    private String tutorialHTML;

    private String thumbnail;

    @OneToMany(mappedBy = "tutorial")
    @JsonIgnore
    private Set<Review> reviewSet;


}
