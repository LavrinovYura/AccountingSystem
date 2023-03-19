package nic.testproject.accountingsystem.dto.contracts;


import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractDTO {
    private String name;
    private ContractType type;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double amount;

    private List<ContractPhaseDTO> phases;
    private List<ContractCounterpartiesDTO> contractCounterparties;
}
