package nic.testproject.accountingsystem.repositories.contracts.projections.update;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateContractDTO {
    private String name;
    private ContractType type;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double amount;
    private List<UpdateContractPhaseDTO> phases;
    private List<UpdateContractCounterpartiesDTO> contractCounterparties;
}
