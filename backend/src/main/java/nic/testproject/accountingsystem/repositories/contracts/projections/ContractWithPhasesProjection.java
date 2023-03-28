package nic.testproject.accountingsystem.repositories.contracts.projections;

import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;

import java.util.List;

public interface ContractWithPhasesProjection {
    List<ContractPhaseProjection> getPhases();
}
