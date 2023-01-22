package com.gary.backendv2.model.users.employees;

import com.gary.backendv2.model.Backup;
import com.gary.backendv2.model.enums.EmployeeType;
import com.gary.backendv2.model.enums.ItemType;
import com.gary.backendv2.model.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEmployee extends User {
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "schedule_id")
    protected WorkSchedule workSchedule;

    @OneToMany(mappedBy = "employee", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EmployeeShift> shifts = new ArrayList<>();

    @OneToMany(mappedBy = "requester")
    private List<Backup> requestedBackup = new ArrayList<>();


    public EmployeeShift getCurrentShift() {
        if (shifts.isEmpty()) {
            return null;
        }

        EmployeeShift shift = shifts.get(shifts.size() - 1);
        for(EmployeeShift e: shifts){
            if(e.getActualStartTime().isBefore(LocalDateTime.now()) && shift.getActualStartTime().isBefore(e.getActualStartTime())){
                shift = e;
            }
        }
        return shift;
    }

    public Boolean getWorking(){
        Optional<EmployeeShift> shiftOptional = Optional.ofNullable(getCurrentShift());
        if (shiftOptional.isEmpty()) {
            return false;
        }

        return getCurrentShift().getActualStartTime().isBefore(LocalDateTime.now()) && getCurrentShift().getActualEndTime() == null ;
    }

    @Transient
    public EmployeeType getDiscriminatorValue() {
        return EmployeeType.fromClass(this.getClass());
    }
}
