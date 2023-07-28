package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ContractCounterpartyRepository extends JpaRepository<ContractCounterparties, Long> {
    List<ContractCounterpartiesProjection> findByPlannedStartDateBetween(LocalDate startDate, LocalDate endDate);
}
