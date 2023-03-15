package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.models.contracts.details.ExpenseType;

import java.math.BigDecimal;

@Data
public class PhaseExpenseDTO {
    private String name;
    private ExpenseType type;
    private BigDecimal amount;
    private ContractPhase contractPhase;
}
