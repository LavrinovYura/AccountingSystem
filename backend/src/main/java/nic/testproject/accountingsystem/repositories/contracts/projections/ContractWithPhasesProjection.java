package nic.testproject.accountingsystem.repositories.contracts.projections;

import java.util.List;

public interface ContractWithPhasesProjection {
    List<ContractPhaseProjection> getPhases();
}
