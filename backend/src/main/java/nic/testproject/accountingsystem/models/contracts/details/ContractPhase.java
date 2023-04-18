package nic.testproject.accountingsystem.models.contracts.details;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nic.testproject.accountingsystem.models.contracts.Contract;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "contract_phases")
@Data
@NoArgsConstructor
public class ContractPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter contract name")
    private String name;

    @NotNull(message = "Please enter the planned start date")
    private LocalDate plannedStartDate;
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

    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    @DecimalMin("0.0")
    private Double actualMaterialCosts;
    @DecimalMin("0.0")
    private Double actualSalaryExpenses;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "contract_id_1")
    private Contract contract1;
}
