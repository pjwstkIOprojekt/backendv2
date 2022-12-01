package com.gary.backendv2.controller;

import com.gary.backendv2.model.dto.request.ReviewRequest;
import com.gary.backendv2.model.dto.request.TutorialRequest;
import com.gary.backendv2.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tutorial")
@RequiredArgsConstructor
public class TutorialController {

    private final TutorialService tutorialService;

    @GetMapping
    public ResponseEntity<?> getAllTutorials() {return ResponseEntity.ok(tutorialService.getAllTutorial());}

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

    @PostMapping("/{tutorialId}/{userId}")
    public void addReviewToTutorial(@PathVariable Integer tutorialId, @PathVariable Integer userId,
                                    @RequestBody @Valid ReviewRequest reviewRequest) {
        tutorialService.addReviewToTutorial(userId,tutorialId,reviewRequest);
    }

    @DeleteMapping("/{tutorialId}/{userId}/{reviewId}")
    public void deleteReviewFromTutorial(@PathVariable Integer tutorialId, @PathVariable Integer userId,
                                         @PathVariable Integer reviewId) {
        tutorialService.deleteReviewFromTutorial(tutorialId, userId, reviewId);
    }

    @PutMapping("/{reviewId}")
    public void updateReview(@PathVariable Integer reviewId, @RequestBody ReviewRequest reviewRequest) {
        tutorialService.updateTutorialReview(reviewId,reviewRequest);
    }
}
