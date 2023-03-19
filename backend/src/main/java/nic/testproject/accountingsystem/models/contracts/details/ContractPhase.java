package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nic.testproject.accountingsystem.models.contracts.Contract;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "contract_phases")
@Data
@NoArgsConstructor
public class ContractPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double phaseCost;
    private Double actualMaterialCosts;
    private Double plannedMaterialCosts;
    private Double actualSalaryExpenses;
    private Double plannedSalaryExpenses;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "contract_id_1")
    private Contract contract1;
}
