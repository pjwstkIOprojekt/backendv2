package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Review;
import com.gary.backendv2.model.Tutorial;
import com.gary.backendv2.model.dto.request.TutorialRequest;
import com.gary.backendv2.model.dto.response.ReviewResponse;
import com.gary.backendv2.model.dto.response.TutorialResponse;
import com.gary.backendv2.model.enums.TutorialType;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.repository.ReviewRepository;
import com.gary.backendv2.repository.TutorialRepository;
import com.gary.backendv2.repository.UserRepository;


import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TutorialServiceTest {
    private final ReviewRepository reviewRepository = mock(ReviewRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TutorialRepository tutorialRepository = mock(TutorialRepository.class);
    private final TutorialService tutorialService = new TutorialService(tutorialRepository, reviewRepository, userRepository);

    @Test
    void getAllTutorials() {
        List<Tutorial> expected = List.of(new Tutorial());
        when(tutorialRepository.findAll()).thenReturn(expected);
        var result = tutorialService.getAllTutorial();
        assertEquals(expected.size(), result.size());
    }

    @Test
    void getTutorialById() {
        Tutorial tutorial = new Tutorial();
        TutorialRequest tutorialRequest = new TutorialRequest();
        tutorialRequest.setTutorialId(2137);
        tutorialRequest.setTutorialType(TutorialType.COURSE);
        tutorialRequest.setTutorialHTML("a");
        tutorialRequest.setName("tutorial");
        tutorialRequest.setThumbnail("b");

        when(tutorialRepository.findById(tutorialRequest.getTutorialId())).thenReturn(Optional.of(tutorial));
        tutorialService.addTutorial(tutorialRequest);

        TutorialResponse result = tutorialService.getTutorialById(2137);

        assertNotNull(result);
        assertEquals(tutorialRequest.getTutorialId(), result.getTutorialId());
    }

    @Test
    void getTutorialByIdShouldNotFind() {
        int tutorialId = 2137;
        when(tutorialRepository.findById(tutorialId)).thenReturn(Optional.empty());
        Exception exc = assertThrows(HttpException.class, () -> {
            tutorialService.getTutorialById(tutorialId);
        });
        String expectedMess = String.format("Cannot find tutorial with %s", tutorialId);
        String actualMess = exc.getMessage();
        assertTrue(actualMess.contains(expectedMess));
    }

    @Test
    void addTutorial() {
        Tutorial expected = new Tutorial();
        TutorialRequest tutorialRequest = new TutorialRequest();
        tutorialRequest.setTutorialId(2137);
        tutorialRequest.setTutorialType(TutorialType.COURSE);
        tutorialRequest.setTutorialHTML("a");
        tutorialRequest.setName("tutorial");
        tutorialRequest.setThumbnail("b");
        tutorialService.addTutorial(tutorialRequest);
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(expected);
        verify(tutorialRepository, times(1)).save(any(Tutorial.class));
    }

    @Test
    void deleteTutorial() {
        int id = 2137;
        Tutorial tutorial = new Tutorial();
        when(tutorialRepository.findById(id)).thenReturn(Optional.of(tutorial));
        tutorialService.deleteTutorial(id);
        verify(tutorialRepository, times(1)).delete(any(Tutorial.class));

    }

    @Test
    void deleteShouldFail() {
        int id = 2137;
        when(tutorialRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(HttpException.class, () -> {
            tutorialService.getTutorialById(id);
        });
        String expMess = String.format("Cannot find tutorial with %s", id);
        String actMess = exception.getMessage();
        assertTrue(actMess.contains(expMess));
    }

    @Test
    void updateShouldWork() {
        int id = 2137;
        Tutorial expected = new Tutorial();
        TutorialRequest tutorialRequest = new TutorialRequest();
        tutorialRequest.setTutorialId(2137);
        tutorialRequest.setTutorialType(TutorialType.COURSE);
        tutorialRequest.setTutorialHTML("a");
        tutorialRequest.setName("tutorial");
        tutorialRequest.setThumbnail("b");
        when(tutorialRepository.findById(id)).thenReturn(Optional.of(expected));
        tutorialService.updateTutorial(tutorialRequest);
        verify(tutorialRepository, times(1)).save(any(Tutorial.class));

    }

    @Test
    void updateShouldFail() {
        int id = 2137;

        TutorialRequest tutorialRequest = new TutorialRequest();
        tutorialRequest.setTutorialId(2137);
        tutorialRequest.setTutorialType(TutorialType.COURSE);
        tutorialRequest.setTutorialHTML("a");
        tutorialRequest.setName("tutorial");
        tutorialRequest.setThumbnail("b");
        Exception exc = assertThrows(HttpException.class, () -> {
            tutorialService.updateTutorial(tutorialRequest);
        });
        String expMess = String.format("Cannot find tutorial with %s", id);
        String actMess = exc.getMessage();
        assertTrue(actMess.contains(expMess));
    }


}

