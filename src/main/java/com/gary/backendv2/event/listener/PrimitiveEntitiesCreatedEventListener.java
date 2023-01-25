package com.gary.backendv2.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.gary.backendv2.event.PrimitiveEntitiesCreatedEvent;
import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.IncidentReportRequest;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.users.employees.Dispatcher;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.DispatcherRepository;
import com.gary.backendv2.service.AmbulanceService;
import com.gary.backendv2.service.EmployeeService;
import com.gary.backendv2.service.IncidentReportService;
import com.gary.backendv2.utils.EnumUtils;
import com.gary.backendv2.utils.Utils;
import com.gary.backendv2.utils.demodata.impl.ObjectInitializationVisitor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class PrimitiveEntitiesCreatedEventListener implements ApplicationListener<PrimitiveEntitiesCreatedEvent> {
    private final DispatcherRepository dispatcherRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final EmployeeService employeeService;
    private final IncidentReportService incidentReportService;
    private final AmbulanceService ambulanceService;

    @Override
    public void onApplicationEvent(PrimitiveEntitiesCreatedEvent event) {
       prepareIncidents(event.getVisitor());
    }

    private void prepareIncidents(ObjectInitializationVisitor visitor) {
        try {
            setDispatchersStateToWorking();
            createSampleIncidents(visitor);
            setDispatchersStateToNotWorking();
        } catch (Exception ignored) {}



    }


    private void setDispatchersStateToWorking() {
        List<Dispatcher> dispatchers = dispatcherRepository.findAll();
        try {
            dispatchers.forEach(employeeService::startShift);
        } catch (HttpException ignored) {}
    }

    private void setDispatchersStateToNotWorking() {
        List<Dispatcher> dispatchers = dispatcherRepository.findAll();
        dispatchers.forEach(employeeService::endShift);
    }

    @SneakyThrows
    private void createSampleIncidents(ObjectInitializationVisitor visitor) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());

        List<BaseRequest> incidentRequests = mapper.readValue(Utils.loadClasspathResource("classpath:dbinit/incident_report_requests.json"), mapper.getTypeFactory().constructCollectionType(List.class, IncidentReportRequest.class));
        new IncidentReport().accept(visitor, incidentReportService, incidentRequests);
    }

}
