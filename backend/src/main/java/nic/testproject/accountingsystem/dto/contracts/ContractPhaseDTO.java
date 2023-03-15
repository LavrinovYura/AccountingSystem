package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.PhaseExpense;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractPhaseDTO {
    private String name;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private Double phaseCost;
    private List<PhaseExpense> expenses;
    private Contract contract;
}
