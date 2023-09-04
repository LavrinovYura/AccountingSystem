package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ContractCounterpartiesDTO {

    @Valid
    @NotNull(message = "Set of Contract Counterparties is empty")
    Set< @Valid ContractCounterpartyDTO> contractCounterparties;
}
