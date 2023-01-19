package com.gary.backendv2.utils.demodata.impl;

import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.model.dto.request.IncidentReportRequest;
import com.gary.backendv2.model.dto.request.items.AbstractCreateItemRequest;
import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import com.gary.backendv2.model.dto.request.users.SignupRequest;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.inventory.items.AmbulanceEquipmentItem;
import com.gary.backendv2.model.inventory.items.MedicineItem;
import com.gary.backendv2.model.inventory.items.MultiUseItem;
import com.gary.backendv2.model.inventory.items.SingleUseItem;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.users.employees.Dispatcher;
import com.gary.backendv2.model.users.employees.Medic;
import com.gary.backendv2.repository.AmbulanceRepository;
import com.gary.backendv2.repository.FacilityRepository;
import com.gary.backendv2.repository.ItemRepository;
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.service.AmbulanceService;
import com.gary.backendv2.service.GeocodingService;
import com.gary.backendv2.service.IncidentReportService;
import com.gary.backendv2.service.ItemService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Entity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ObjectInitializationVisitor implements EntityVisitor {
    private final FacilityRepository facilityRepository;
    private final AmbulanceRepository ambulanceRepository;

    @Override
    public void visit(Facility facility, GeocodingService geocodingService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            facilityRepository.save(facility.create((FacilityRequest) baseRequest, geocodingService));
        }
    }

    @Override
    public void visit(Ambulance ambulance, AmbulanceService ambulanceService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            ambulanceRepository.save(ambulance.create((AddAmbulanceRequest) baseRequest, ambulanceService));
        }
    }

    @Override
    public void visit(User user, AuthService authService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            authService.registerUser((SignupRequest) baseRequest);
        }
    }

    @Override
    public void visit(Medic medic, AuthService authService, EmployeeType employeeType, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            authService.registerEmployee(employeeType, (RegisterEmployeeRequest) baseRequest);
        }
    }

    @Override
    public void visit(Dispatcher dispatcher, AuthService authService, EmployeeType employeeType, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            authService.registerEmployee(employeeType, (RegisterEmployeeRequest) baseRequest);
        }
    }

    @Override
    public void visit(IncidentReport incidentReport, IncidentReportService incidentReportService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            incidentReportService.add((IncidentReportRequest) baseRequest, true);
        }
    }

    @Override
    public void visit(SingleUseItem singleUseItem, ItemService itemService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            itemService.createItem((AbstractCreateItemRequest) baseRequest);
        }
    }

    @Override
    public void visit(MultiUseItem multiUseItem, ItemService itemService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            itemService.createItem((AbstractCreateItemRequest) baseRequest);
        }
    }

    @Override
    public void visit(AmbulanceEquipmentItem multiUseItem, ItemService itemService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            itemService.createItem((AbstractCreateItemRequest) baseRequest);
        }
    }

    @Override
    public void visit(MedicineItem multiUseItem, ItemService itemService, List<BaseRequest> baseRequests) {
        for (BaseRequest baseRequest : baseRequests) {
            itemService.createItem((AbstractCreateItemRequest) baseRequest);
        }
    }

}
