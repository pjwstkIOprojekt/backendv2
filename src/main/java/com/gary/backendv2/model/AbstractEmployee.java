package com.gary.backendv2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEmployee extends User {
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "schedule_id")
    protected WorkSchedule workSchedule;

}
