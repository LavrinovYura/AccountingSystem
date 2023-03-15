package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractPhasesRepository extends JpaRepository<ContractPhase, Long> {

}
