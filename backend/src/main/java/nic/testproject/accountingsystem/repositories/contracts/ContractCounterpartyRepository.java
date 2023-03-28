package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractCounterpartyRepository extends JpaRepository<ContractCounterparties, Long> {
    Optional<ContractCounterparties> findByContract2(Contract contract);
    Optional<List<ContractCounterpartiesProjection>> findByPlannedStartDateBetweenOrderByPlannedStartDateAsc(LocalDate startDate, LocalDate endDate);

}