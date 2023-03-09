package nic.testproject.accountingsystem.dto.contracts;


import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.counterparty.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.models.contracts.details.ContractType;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractDTO {
    private String name;
    private ContractType type;
    private LocalDate planedStartDate;
    private LocalDate planedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double amount;
    private List<ContractPhase> phases;
    private List<ContractCounterparties> contractCounterparties;
}
