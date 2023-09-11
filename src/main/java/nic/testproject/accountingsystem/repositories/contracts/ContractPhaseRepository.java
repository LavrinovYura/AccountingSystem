package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractPhaseRepository extends JpaRepository<ContractPhase, Long> {
}
