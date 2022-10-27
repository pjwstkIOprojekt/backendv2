package com.gary.backendv2.model.enums;

public enum RoleName {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String prefixedName;

    RoleName(String name) {
        this.prefixedName = name;
    }

    public String getPrefixedName() {
        return prefixedName;
    }

}
