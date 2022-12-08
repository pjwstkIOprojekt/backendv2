package com.gary.backendv2.model;

import com.gary.backendv2.model.users.User;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "review")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tutorial_id")
    private Tutorial tutorial;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reviewer;

    private Double value;

    private String reviewDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(tutorial, review.tutorial) && Objects.equals(reviewer, review.reviewer) && Objects.equals(value, review.value) && Objects.equals(reviewDescription, review.reviewDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tutorial, reviewer, value, reviewDescription);
    }
}
