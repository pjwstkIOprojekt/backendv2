package com.gary.backendv2.model.users.employees;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class WorkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer scheduleId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @JsonIgnore
    private String schedule;

    @Transient
    private MappedSchedule mappedSchedule;

    @JsonIgnore
    private LocalDateTime createdAt;

    public void setSchedule(String json) {
        this.schedule = json;
        this.mappedSchedule = MappedSchedule.fromJson(json);
    }

    public MappedSchedule getMappedSchedule() {
        return MappedSchedule.fromJson(this.schedule);
    }
}
