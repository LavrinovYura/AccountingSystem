package nic.testproject.accountingsystem.dto.contracts;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractDTO {
    private String name;
    private ContractType type;
    private String plannedStartDate;
    private String plannedEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private Double amount;

    private List<ContractPhaseDTO> phases;
    private List<ContractCounterpartiesDTO> contractCounterparties;
}
