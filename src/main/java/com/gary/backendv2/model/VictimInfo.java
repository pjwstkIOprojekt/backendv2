package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.Gender;
import com.gary.backendv2.model.enums.VictimStatus;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.users.employees.Medic;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class VictimInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer victimInfoId;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private VictimStatus victimStatus;

    @OneToOne
    private User medic;
}
