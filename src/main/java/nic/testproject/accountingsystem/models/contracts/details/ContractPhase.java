package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nic.testproject.accountingsystem.models.contracts.Contract;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contract_phases")
@Getter
@Setter
@NoArgsConstructor
public class ContractPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private Double plannedMaterialCosts;
    private Double phaseCost;
    private Double plannedSalaryExpenses;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double actualMaterialCosts;
    private Double actualSalaryExpenses;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
