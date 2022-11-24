package com.gary.backendv2.model;

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
    private User user;

    private Double value;

    private String discription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(tutorial, review.tutorial) && Objects.equals(user, review.user) && Objects.equals(value, review.value) && Objects.equals(discription, review.discription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tutorial, user, value, discription);
    }
}
