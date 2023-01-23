package com.gary.backendv2.model.users.employees;

import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.users.SignupRequest;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.utils.demodata.EntityVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class AmbulanceManager extends AbstractEmployee {
    public Optional<User> create(SignupRequest addRequest, AuthService authService) {
        authService.registerMedic(addRequest);

        return Optional.empty();
    }

    public void accept(EntityVisitor ev, AuthService authService, EmployeeType employeeType, List<BaseRequest> baseRequest) {
        ev.visit(this, authService, employeeType, baseRequest);
    }
}
