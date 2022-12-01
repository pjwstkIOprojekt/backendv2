package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Review;
import com.gary.backendv2.model.Tutorial;
import com.gary.backendv2.model.User;
import com.gary.backendv2.model.dto.request.ReviewRequest;
import com.gary.backendv2.model.dto.request.TutorialRequest;
import com.gary.backendv2.model.dto.response.TutorialResponse;
import com.gary.backendv2.repository.ReviewRepository;
import com.gary.backendv2.repository.TutorialRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            OptionalDouble avarageRating = t.getReviewSet().stream().mapToDouble(Review::getValue).average();
            tutorialResponses.add(
                    TutorialResponse
                            .builder()
                            .tutorialId(t.getTutorialId())
                            .tutorialType(t.getTutorialType())
                            .name(t.getName())
                            .avarageRating(avarageRating.isPresent() ? avarageRating.getAsDouble() : 0.0)
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
                .avarageRating(avarageRating.isPresent() ? avarageRating.getAsDouble() : 0.0)
                .tutorialHTML(t.getTutorialHTML())
                .build();
    }

    public void addTutorial(TutorialRequest tutorialRequest) {

        Tutorial tutorial = Tutorial.builder()
                .tutorialType(tutorialRequest.getTutorialType())
                .name(tutorialRequest.getName())
                .tutorialHTML(tutorialRequest.getTutorialHTML())
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
        tutorialRepository.save(t);
    }


    public void addReviewToTutorial(Integer userId, Integer tutorialId, ReviewRequest reviewRequest) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialId));
        }
        Tutorial t = optionalTutorial.get();

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", userId));
        }
        User u = optionalUser.get();

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
    }

    public void deleteReviewFromTutorial(Integer userId, Integer tutorialId, Integer reviewId){
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(tutorialId);
        if (optionalTutorial.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find tutorial with %s", tutorialId));
        }
        Tutorial tutorial = optionalTutorial.get();

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find user with %s", userId));
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
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find review with %s", reviewId));
        }
        Review review = optionalReview.get();
        review.setValue(reviewRequest.getValue());
        review.setReviewDescription(review.getReviewDescription());
        reviewRepository.save(review);
    }


}
