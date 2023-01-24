package com.gary.backendv2.repository.projections;

import com.gary.backendv2.model.enums.Gender;
import com.gary.backendv2.model.enums.VictimStatus;

import java.util.List;

/**
 * A Projection for the {@link com.gary.backendv2.model.incident.Incident} entity
 */
public interface IncidentInfo {
    Integer getIncidentId();

    List<VictimInfoInfo> getVictims();

    /**
     * A Projection for the {@link com.gary.backendv2.model.VictimInfo} entity
     */
    interface VictimInfoInfo {
        Integer getVictimInfoId();

        String getFirstName();

        String getLastName();

        Gender getGender();

        VictimStatus getVictimStatus();

        UserInfo getMedic();

        /**
         * A Projection for the {@link com.gary.backendv2.model.users.User} entity
         */
        interface UserInfo {
            Integer getUserId();

            String getEmail();

            String getFirstName();

            String getLastName();
        }
    }
}