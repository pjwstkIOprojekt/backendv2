package com.gary.backendv2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEmployee extends User {
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "schedule_id")
    protected WorkSchedule workSchedule;

    @OneToMany(mappedBy = "employee", orphanRemoval = true)
    private List<EmployeeShift> shifts = new ArrayList<>();


    public EmployeeShift getCurrentShift() {
        EmployeeShift shift = shifts.get(shifts.size() - 1);
        for(EmployeeShift e: shifts){
            if(e.getActualStartTime().isBefore(LocalDateTime.now()) && shift.getActualStartTime().isBefore(e.getActualStartTime())){
                shift = e;
            }
        }
        return shift;
    }

    public Boolean getWorking(){
        return getCurrentShift().getActualStartTime().isBefore(LocalDateTime.now()) && getCurrentShift().getActualEndTime() == null ;
    }
}
