package nic.testproject.accountingsystem.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractProjection;

import java.util.List;

@Data
@AllArgsConstructor
public class AllContracts {
        private List<ContractProjection> contracts;
        private List<ContractCounterpartiesProjection> contractCounterparties;
}
