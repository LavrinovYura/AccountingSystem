package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nic.testproject.accountingsystem.models.contracts.Contract;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank(message = "Please enter contract name")
    private String name;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Please enter the planned start date")
    private LocalDate plannedStartDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Please enter the planned end date")
    private LocalDate plannedEndDate;

    @NotNull(message = "Please enter the planned costs of materials")
    @DecimalMin("0.0")
    private Double plannedMaterialCosts;
    @NotNull(message = "Please enter the planned expenditures for salaries")
    @DecimalMin("0.0")
    private Double phaseCost;
    @NotNull(message = "Please enter the planned expenses for the phase")
    @DecimalMin("0.0")
    private Double plannedSalaryExpenses;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualStartDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate actualEndDate;

    @DecimalMin("0.0")
    private Double actualMaterialCosts;

    @DecimalMin("0.0")
    private Double actualSalaryExpenses;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
