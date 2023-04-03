package nic.testproject.accountingsystem.repositories.contracts.projections.update;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.Contract;

import java.time.LocalDate;

@Data
public class UpdateContractPhaseDTO {
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
}
