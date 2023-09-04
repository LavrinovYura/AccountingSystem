package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractWithPhasesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {
    boolean existsByName(String name);
    Optional<ContractWithPhasesProjection> findContractById(Long id);
    List<ContractProjection> findByPlannedStartDateBetween(LocalDate startDate, LocalDate endDate);
}
