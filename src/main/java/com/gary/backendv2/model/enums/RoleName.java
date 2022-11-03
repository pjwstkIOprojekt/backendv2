package com.gary.backendv2.model.enums;

public enum RoleName {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    PARAMEDIC("ROLE_PARAMEDIC"),
    DISPATCHER("ROLE_DISPATCHER"),
    AMBULANCE_MANAGER("ROLE_AMBULANCE_MANAGER");

    private final String prefixedName;

    RoleName(String name) {
        this.prefixedName = name;
    }

    public String getPrefixedName() {
        return prefixedName;
    }

}
