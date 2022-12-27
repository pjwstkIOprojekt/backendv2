package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.ReviewRequest;
import com.gary.backendv2.model.dto.request.TutorialRequest;
import com.gary.backendv2.service.TutorialService;
import com.gary.backendv2.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/tutorial")
@RequiredArgsConstructor
public class TutorialController {

    private final TutorialService tutorialService;

    @GetMapping
    public ResponseEntity<?> getAllTutorials() {return ResponseEntity.ok(tutorialService.getAllTutorial());}

    @GetMapping("/css")
    public ResponseEntity<?> getCss(){ return ResponseEntity.ok(Utils.getTutorialCss()); }

    @GetMapping("/{tutorialId}")
    public ResponseEntity<?> getTutorialById(@PathVariable Integer tutorialId){
        return ResponseEntity.ok(tutorialService.getTutorialById(tutorialId));
    }
    @PostMapping
    public void addTutorial(@RequestBody @Valid TutorialRequest tutorialRequest) {
        tutorialService.addTutorial(tutorialRequest);
    }

    @PutMapping
    public void updateTutorial(@RequestBody @Valid TutorialRequest tutorialRequest) {
        tutorialService.updateTutorial(tutorialRequest);
    }

    @DeleteMapping("/{tutorialId}")
    public void deleteTutorial(@PathVariable Integer tutorialId) {
        tutorialService.deleteTutorial(tutorialId);
    }

    @PostMapping("/{tutorialId}/{email}")
    public void addReviewToTutorial(@PathVariable Integer tutorialId, @PathVariable @Email String email,
                                    @RequestBody @Valid ReviewRequest reviewRequest) {
        tutorialService.addReviewToTutorial(email,tutorialId,reviewRequest);
    }

    @DeleteMapping("/{tutorialId}/{email}/{reviewId}")
    public void deleteReviewFromTutorial(@PathVariable Integer tutorialId, @PathVariable @Email String email,
                                         @PathVariable Integer reviewId) {
        tutorialService.deleteReviewFromTutorial(email, tutorialId, reviewId);
    }

    @PutMapping("/{reviewId}")
    public void updateReview(@PathVariable Integer reviewId, @RequestBody ReviewRequest reviewRequest) {
        tutorialService.updateTutorialReview(reviewId,reviewRequest);
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable Integer reviewId) {return ResponseEntity.ok(tutorialService.getReviewById(reviewId));}

    @GetMapping("/{tutorialId}/reviews")
    public ResponseEntity<?> getAllReviewsForTutorial(@PathVariable Integer tutorialId) {return ResponseEntity.ok(tutorialService.getAllReviewsForTutorial(tutorialId));}
}