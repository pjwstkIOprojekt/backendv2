package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Review;
import com.gary.backendv2.model.Tutorial;
import com.gary.backendv2.model.dto.response.ReviewResponse;
import com.gary.backendv2.model.dto.response.users.GenericUserResponse;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.dto.request.ReviewRequest;
import com.gary.backendv2.model.dto.request.TutorialRequest;
import com.gary.backendv2.model.dto.response.TutorialResponse;
import com.gary.backendv2.repository.ReviewRepository;
import com.gary.backendv2.repository.TutorialRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class TutorialService {

    private final TutorialRepository tutorialRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<TutorialResponse> getAllTutorial() {
        List<Tutorial> tutorials = tutorialRepository.findAll();
        List<TutorialResponse> tutorialResponses = new ArrayList<>();
        for (Tutorial t : tutorials) {
            OptionalDouble averageRating = t.getReviewSet().stream().mapToDouble(Review::getValue).average();
            tutorialResponses.add(
                    TutorialResponse
                            .builder()
                            .tutorialId(t.getTutorialId())
                            .tutorialType(t.getTutorialType())
                            .name(t.getName())
                            .averageRating(averageRating.isPresent() ? averageRating.getAsDouble() : 0.0)
                            .thumbnail(t.getThumbnail())
                            .tutorialHTML(t.getTutorialHTML())
                            .build()
            );
        }
        return tutorialResponses;
    }

    public TutorialResponse getTutorialById(Integer tutorialId) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialId));
        }
        Tutorial t = optionalTutorial.get();
        OptionalDouble avarageRating = t.getReviewSet().stream().mapToDouble(Review::getValue).average();
        return TutorialResponse
                .builder()
                .tutorialId(t.getTutorialId())
                .tutorialType(t.getTutorialType())
                .name(t.getName())
                .averageRating(avarageRating.isPresent() ? avarageRating.getAsDouble() : 0.0)
                .tutorialHTML(t.getTutorialHTML())
                .thumbnail(t.getThumbnail())
                .build();
    }

    public void addTutorial(TutorialRequest tutorialRequest) {

        Tutorial tutorial = Tutorial.builder()
                .tutorialType(tutorialRequest.getTutorialType())
                .name(tutorialRequest.getName())
                .tutorialHTML(tutorialRequest.getTutorialHTML())
                .thumbnail(tutorialRequest.getThumbnail())
                .build();
        tutorialRepository.save(tutorial);
    }

    public void deleteTutorial(Integer tutorialId) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialId));
        }
        Tutorial t = optionalTutorial.get();
        tutorialRepository.delete(t);
    }

    public void updateTutorial(TutorialRequest tutorialRequest) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialRequest.getTutorialId());
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialRequest.getTutorialId()));
        }
        Tutorial t = optionalTutorial.get();
        t.setTutorialType(tutorialRequest.getTutorialType());
        t.setName(tutorialRequest.getName());
        t.setTutorialHTML(tutorialRequest.getTutorialHTML());
        t.setThumbnail(tutorialRequest.getThumbnail());
        tutorialRepository.save(t);
    }


    public void addReviewToTutorial(String email, Integer tutorialId, ReviewRequest reviewRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", email));
        }
        User u = optionalUser.get();

        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialId));
        }
        Tutorial t = optionalTutorial.get();
        if (t.getReviewSet().stream().anyMatch(x -> x.getReviewer().equals(u))) {
            // This tutorial had been already reviewed by this user, so update the score
            Review r = t.getReviewSet().stream().filter(x -> x.getReviewer().equals(u)).findFirst().orElseThrow(() -> {throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Unidentified error whilst editing the review");});
            r.setValue(reviewRequest.getValue());

            reviewRepository.save(r);
            return;
        }

        Review review = Review.builder()
                .reviewer(u)
                .tutorial(t)
                .value(reviewRequest.getValue())
                .reviewDescription(reviewRequest.getDiscription())
                .build();

        u.getReviewSet().add(review);
        t.getReviewSet().add(review);

        userRepository.save(u);
        tutorialRepository.save(t);
        reviewRepository.save(review);
    }

    public void deleteReviewFromTutorial(String email, Integer tutorialId, Integer reviewId){
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialId));
        }
        Tutorial tutorial = optionalTutorial.get();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", email));
        }
        User user = optionalUser.get();

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find review with %s", reviewId));
        }
        Review review = optionalReview.get();

        // delete only if delete request comes from poster or an admin TODO: admin branch not implemented yet
        if (!review.getReviewer().getUserId().equals(user.getUserId())) {
            return;
        }

        user.getReviewSet().remove(review);
        tutorial.getReviewSet().remove(review);
        userRepository.save(user);
        tutorialRepository.save(tutorial);
        reviewRepository.delete(review);
    }

    public void updateTutorialReview(Integer reviewId, ReviewRequest reviewRequest) {
        User reviewer;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserPrincipal loggedPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            reviewer = userRepository.getByEmail(loggedPrincipal.getUsername());
        } else throw new HttpException(HttpStatus.FORBIDDEN, "This user cannot edit reviews");

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find review with %s", reviewId));
        }

        Review review = optionalReview.get();
        if (review.getReviewer().equals(reviewer)) {
            review.setValue(reviewRequest.getValue());
            review.setReviewDescription(review.getReviewDescription());
            reviewRepository.save(review);
        }
    }


    public ReviewResponse getReviewById(Integer reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find review with %s", reviewId));
        }
        Review review = optionalReview.get();

        System.out.println(review.getId());

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .tutorial(TutorialResponse.of(review.getTutorial()))
                .reviewer(GenericUserResponse.of(review.getReviewer()))
                .reviewDescription(review.getReviewDescription())
                .value(review.getValue())
                .build();
    }

    public List<ReviewResponse> getAllReviewsForTutorial(Integer tutorialId) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialId));
        }
        Tutorial tutorial = optionalTutorial.get();
        List<ReviewResponse> tutorialReviews = new ArrayList<>();
        for (Review review : tutorial.getReviewSet()) {
            tutorialReviews.add(
                    ReviewResponse.builder()
                            .reviewId(review.getId())
                            .tutorial(TutorialResponse.of(review.getTutorial()))
                            .reviewer(GenericUserResponse.of(review.getReviewer()))
                            .reviewDescription(review.getReviewDescription())
                            .value(review.getValue())
                            .build());
        }
        return tutorialReviews;
    }
}