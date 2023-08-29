package nic.testproject.accountingsystem.repositories.contracts.projections;

import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;

public interface ContractCounterpartiesProjection {
    String getName();
    Double getAmount();
    LocalDate getPlannedStartDate();
    LocalDate getPlannedEndDate();
    LocalDate getActualStartDate();
    LocalDate getActualEndDate();
    ContractType getType();
    ContractProjection getContract();
}
