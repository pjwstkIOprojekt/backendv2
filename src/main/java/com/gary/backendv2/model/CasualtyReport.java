package com.gary.backendv2.model;

import com.gary.backendv2.model.incident.Incident;
import lombok.*;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CasualtyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "casulatyReportId")
    private Integer id;

    private String description;

    @OneToOne(mappedBy = "casualtyReport")
    private Incident incident;

    @OneToMany(mappedBy = "casualtyReport")
    private Set<Facility> facilities;

    @ElementCollection
    @MapKeyColumn(name = "item_id")
    @Column(name = "count")
    @CollectionTable(name = "casualty_report_item_counts", joinColumns = @JoinColumn(name = "casulatyReportId"))
    private Map<Integer, Integer> itemCounts;

}
