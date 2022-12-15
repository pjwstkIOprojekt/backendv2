package com.gary.backendv2.service;

import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.EventReport;
import com.gary.backendv2.model.dto.request.EventReportRequest;
import com.gary.backendv2.model.dto.response.EventReportResponse;
import com.gary.backendv2.repository.EventReportRepository;
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
                            .reporter(e.getReporter())
                            .description(e.getDescription())
                            .build()
            );
        }
        return eventReportResponses;
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
                .reporter(e.getReporter())
                .description(e.getDescription())
                .build();
    }

    public void addReportEvent(EventReportRequest eventReportRequest) {
        EventReport e = EventReport.builder()
                .date(LocalDateTime.now())
                .dangerScale(eventReportRequest.getDangerScale())
                .location(eventReportRequest.getLocation())
                .emergencyType(eventReportRequest.getEmergencyType())
                .reporter(eventReportRequest.getReporter())
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