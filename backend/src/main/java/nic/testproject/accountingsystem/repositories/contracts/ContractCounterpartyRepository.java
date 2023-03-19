package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractCounterpartyRepository extends JpaRepository<ContractCounterparties, Long> {
    ContractCounterparties findByContract(Contract contract);
}
