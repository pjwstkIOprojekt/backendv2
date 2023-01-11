package com.gary.backendv2.factories.impl;

import com.gary.backendv2.factories.asbtract.CasualtyReportAbstractFactory;
import com.gary.backendv2.model.CasualtyReport;
import com.gary.backendv2.model.incident.Incident;

public class CasualtyReportFactory implements CasualtyReportAbstractFactory {

    @Override
    public CasualtyReport create(Incident incident) {
        CasualtyReport casualtyReport = new CasualtyReport();
        casualtyReport.setIncident(incident);
        return casualtyReport;
    }
}
