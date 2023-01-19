package com.gary.backendv2.utils.demodata;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.BaseRequest;
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
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.service.AmbulanceService;
import com.gary.backendv2.service.IncidentReportService;
import com.gary.backendv2.service.ItemService;

import java.util.List;

public interface EntityVisitor {
    void visit(Facility facility, List<BaseRequest> baseRequests);
    void visit(Ambulance ambulance, AmbulanceService ambulanceService, List<BaseRequest> baseRequests);
    void visit(User user, AuthService authService, List<BaseRequest> baseRequests);
    void visit(Medic medic, AuthService authService, EmployeeType employeeType, List<BaseRequest> baseRequests);
    void visit(Dispatcher medic, AuthService authService, EmployeeType employeeType, List<BaseRequest> baseRequests);
    void visit(IncidentReport incidentReport, IncidentReportService incidentReportService, List<BaseRequest> baseRequests);
    void visit(SingleUseItem singleUseItem, ItemService itemService, List<BaseRequest> baseRequests);
    void visit(MultiUseItem multiUseItem, ItemService itemService, List<BaseRequest> baseRequests);
    void visit(AmbulanceEquipmentItem multiUseItem, ItemService itemService, List<BaseRequest> baseRequests);
    void visit(MedicineItem multiUseItem, ItemService itemService, List<BaseRequest> baseRequests);
}
