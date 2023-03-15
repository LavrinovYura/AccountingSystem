package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    boolean existsByName(String name);

    Optional<Contract> getContractById(Long id);

    Optional<List<Contract>> findByType(ContractType type);

    Optional<List<Contract>> findByPlannedStartDate(LocalDate startDate);

    Optional<List<Contract>> findByPlannedEndDate(LocalDate endDate);

    Optional<List<Contract>> findByPlannedStartDateAndPlannedEndDate(LocalDate startDate, LocalDate endDate);

    Optional<List<Contract>> findByActualStartDate(LocalDate startDate);

    Optional<List<Contract>> findByActualEndDate(LocalDate endDate);

    Optional<List<Contract>> findByActualStartDateAndPlannedEndDate(LocalDate startDate, LocalDate endDate);

    Optional<List<Contract>> findByAmount(Double amount);

    List<Contract> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);
}
