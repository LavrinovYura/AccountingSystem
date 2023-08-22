package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;

@Data
public class ContractCriteriaDTO {
    private String name;
    private ContractType type;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double amount;
}
