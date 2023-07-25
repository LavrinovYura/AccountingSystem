package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.Contract;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractPhaseDTO {
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
