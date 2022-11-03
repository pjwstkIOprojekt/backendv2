package com.gary.backendv2.security;

import com.gary.backendv2.model.enums.RoleName;
import org.springframework.stereotype.Component;

@Component
public class RoleOrder {
    private final StringBuilder roleDefinitionBuffer = new StringBuilder();

    /**
     * Creates single Spring Security Role Hierarchy rule
     * @param roles all roles to define order to them, first passed is master role, rest are dependents
     * @return Single Spring Security role dependency definition
     */
    public void addRule(RoleName... roles) throws Exception {
        if (roles.length < 2) {
            throw new Exception("Not enough roles provided");
        }

        for (int i = 0; i < roles.length; i++) {
            if (i == roles.length - 1) {
                roleDefinitionBuffer.append(String.format("%s \n", roles[i].getPrefixedName()));

                return;
            }

            roleDefinitionBuffer.append(String.format("%s > ", roles[i].getPrefixedName()));

        }

        throw new Exception("Malformed role hierarchy rule definition");
    }

    public String getRoleHierarchyOrder() {
        return roleDefinitionBuffer.toString();
    }
}
