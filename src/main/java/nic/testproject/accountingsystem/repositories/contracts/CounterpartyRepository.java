package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CounterpartyRepository extends JpaRepository<Counterparty, Long>, JpaSpecificationExecutor<Counterparty> {
    Optional<Counterparty> findByName(String name);

    Optional<Counterparty> findById(Long id);
    boolean existsByName(String name);
    boolean existsByInn(String inn);
}
