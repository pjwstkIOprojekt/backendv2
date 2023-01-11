package com.gary.backendv2.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionOfMaterials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "casualty_report_id")
    private CasualtyReport casualtyReport;

    @OneToMany(mappedBy = "consumptionOfMaterials", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Integer, Consumption> items;
}
