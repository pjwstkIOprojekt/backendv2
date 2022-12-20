package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.EventReport;
import com.gary.backendv2.model.dto.request.EventReportRequest;
import com.gary.backendv2.model.dto.response.EventReportResponse;
import com.gary.backendv2.model.dto.response.users.UserResponse;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.repository.EventReportRepository;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventReportService {

    private final EventReportRepository eventReportRepository;

    private final UserRepository userRepository;

    public List<EventReportResponse> getAllReportedEvents() {
        List<EventReport> eventReports = eventReportRepository.findAll();
        List<EventReportResponse> eventReportResponses = new ArrayList<>();

        for (EventReport e : eventReports) {
            eventReportResponses.add(
                    EventReportResponse.builder()
                            .id(e.getId())
                            .date(e.getDate())
                            .dangerScale(e.getDangerScale())
                            .location(e.getLocation())
                            .emergencyType(e.getEmergencyType())
                            .userResponse(getUserResponse(e))
                            .description(e.getDescription())
                            .build()
            );
        }
        return eventReportResponses;
    }

    private UserResponse getUserResponse(EventReport e) {
        User u = e.getReporter();
        return UserResponse.builder()
                .email(u.getEmail())
                .birthDate(u.getBirthDate())
                .phoneNumber(u.getPhoneNumber())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .build();
    }

    public EventReportResponse getReportedEventById(Integer id){
        Optional<EventReport> eventReportOptional= eventReportRepository.findById(id);
        if (eventReportOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find reported event with %s", id));
        }
        EventReport e = eventReportOptional.get();
        return EventReportResponse.builder()
                .id(e.getId())
                .date(e.getDate())
                .dangerScale(e.getDangerScale())
                .location(e.getLocation())
                .emergencyType(e.getEmergencyType())
                .userResponse(getUserResponse(e))
                .description(e.getDescription())
                .build();
    }

    public void addReportEvent(EventReportRequest eventReportRequest) {
        EventReport e = EventReport.builder()
                .date(LocalDateTime.now())
                .dangerScale(eventReportRequest.getDangerScale())
                .location(eventReportRequest.getLocation())
                .emergencyType(eventReportRequest.getEmergencyType())
                .reporter((userRepository.findByEmail(eventReportRequest.getEmail())).get())
                .description(eventReportRequest.getDescription())
                .build();
        eventReportRepository.save(e);
    }

    public void updateEventReport(Integer id,EventReportRequest eventReportRequest ) {
        Optional<EventReport> eventReportOptional= eventReportRepository.findById(id);
        if (eventReportOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find reported event with %s", id));
        }
        EventReport e = eventReportOptional.get();
        e.setDate(eventReportRequest.getDate());
        e.setDangerScale(eventReportRequest.getDangerScale());
        e.setLocation(eventReportRequest.getLocation());
        e.setEmergencyType(eventReportRequest.getEmergencyType());
        e.setDescription(eventReportRequest.getDescription());
        eventReportRepository.save(e);
    }

    public void deleteEventReport(Integer id) {
        Optional<EventReport> eventReportOptional= eventReportRepository.findById(id);
        if (eventReportOptional.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, String.format("Cannot find reported event with %s", id));
        }
        EventReport e = eventReportOptional.get();
        eventReportRepository.delete(e);

    }


}