package nic.testproject.accountingsystem.repositories.contracts.projections;

import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;

public interface ContractProjection {
    String getName();
    ContractType getType();
    LocalDate getPlannedStartDate();
    LocalDate getPlannedEndDate();
    LocalDate getActualStartDate();
    LocalDate getActualEndDate();
    Double getAmount();
}
