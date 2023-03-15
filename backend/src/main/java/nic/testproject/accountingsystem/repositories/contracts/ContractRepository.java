package nic.testproject.accountingsystem.repositories.contracts;

import nic.testproject.accountingsystem.models.contracts.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {

    boolean existsByName(String name);

}
