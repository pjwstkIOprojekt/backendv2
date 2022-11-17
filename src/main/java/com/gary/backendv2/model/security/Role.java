package com.gary.backendv2.model.security;

import com.gary.backendv2.model.User;
import com.gary.backendv2.model.enums.RoleName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;

    @Override
    public String getAuthority() {
        return String.valueOf(name);
    }

    @ManyToMany(mappedBy = "roles")
    Set<User> users;
}
