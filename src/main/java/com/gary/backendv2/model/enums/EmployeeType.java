package com.gary.backendv2.model.enums;

import com.gary.backendv2.model.users.employees.AmbulanceManager;
import com.gary.backendv2.model.users.employees.Dispatcher;
import com.gary.backendv2.model.users.employees.Medic;

public enum EmployeeType {
    DISPATCHER(Dispatcher.class),
    MEDIC(Medic.class),
    AMBULANCE_MANAGER(AmbulanceManager.class);

    private final Class<?> mappedClass;

    EmployeeType(Class<?> clazz) {
        this.mappedClass = clazz;
    }

    public Class<?> getMappedClass() {
        return mappedClass;
    }

    public static EmployeeType fromClass(Class<?> clazz) {
        for (EmployeeType employeeType : EmployeeType.values()) {
            if (employeeType.mappedClass.equals(clazz)) {
                return employeeType;
            }
        }

        return null;
    }
}
