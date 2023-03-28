package nic.testproject.accountingsystem.repositories.contracts.projections;

import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;

public interface ContractPhaseProjection {
    String getName();
    LocalDate getPlannedStartDate();
    LocalDate getPlannedEndDate();
    LocalDate getActualStartDate();
    LocalDate getActualEndDate();
    Double getPhaseCost();
    Double getActualMaterialCosts();
    Double getPlannedMaterialCosts();
    Double getActualSalaryExpenses();
    Double getPlannedSalaryExpenses();
}
