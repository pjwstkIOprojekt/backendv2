package com.gary.backendv2.listener;

import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.service.CasualtyReportService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;


public class IncidentStatusTypeListener {

    @Autowired
    private CasualtyReportService casualtyReportService;

    @PreUpdate
    public void ambulanceAssigned(Incident incident){
        if (incident.getIncidentStatusType() == IncidentStatusType.ASSIGNED && incident.getAmbulances().size() > 0){
            casualtyReportService.generateCasualtyReport(incident);
        }
    }
    @PostUpdate
    public void incidentClosed(Incident incident) {
        if (incident.getIncidentStatusType() == IncidentStatusType.CLOSED) {
            casualtyReportService.updateStateOfInventory(incident);
        }
    }
}
