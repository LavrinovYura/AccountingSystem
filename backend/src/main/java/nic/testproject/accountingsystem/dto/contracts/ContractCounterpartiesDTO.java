package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.models.contracts.ContractType;

import java.time.LocalDate;

@Data
public class ContractCounterpartiesDTO {
    private String name;
    private ContractType type;
    private Double amount;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    private CounterpartyDTO counterparty;
}
