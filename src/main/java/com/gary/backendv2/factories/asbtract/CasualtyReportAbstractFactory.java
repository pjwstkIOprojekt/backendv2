package com.gary.backendv2.factories.asbtract;

import com.gary.backendv2.model.CasualtyReport;
import com.gary.backendv2.model.incident.Incident;

public interface CasualtyReportAbstractFactory {

    CasualtyReport create(Incident incident);
}
