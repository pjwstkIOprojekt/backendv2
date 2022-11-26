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
        return shifts.get(shifts.size() - 1);
    }

    public Boolean getWorking(){
        if(shifts.get(shifts.size() - 1).getActualStartTime().isAfter(LocalDateTime.now()) && shifts.get(shifts.size() - 1).getActualEndTime() == null){
            return true;
        }else{
            return false;
        }
    }
}
