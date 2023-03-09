package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    boolean existsByName(String name);

    Optional<Contract> findById(int id);

    Optional<List<Contract>> findByType(ContractType type);

    Optional<List<Contract>> findByPlanedStartDate(LocalDate startDate);

    Optional<List<Contract>> findByPlanedEndDate(LocalDate endDate);

    Optional<List<Contract>> findByPlanedStartDateAndPlanedEndDate(LocalDate startDate, LocalDate endDate);

    Optional<List<Contract>> findByActualStartDate(LocalDate startDate);

    Optional<List<Contract>> findByActualEndDate(LocalDate endDate);

    Optional<List<Contract>> findByActualStartDateAndPlanedEndDate(LocalDate startDate, LocalDate endDate);

    Optional<List<Contract>> findByAmount(Double amount);

    @Query("SELECT c FROM Contract c JOIN c.phases p WHERE p.name = :phaseName")
    Optional<List<Contract>> findByPhaseName(@Param("phaseName") String phaseName);

    @Query("SELECT c FROM Contract c JOIN c.contractCounterparties cp WHERE cp.name = :counterpartyName")
    Optional<List<Contract>> findByCounterpartyName(@Param("counterpartyName") String counterpartyName);
}
