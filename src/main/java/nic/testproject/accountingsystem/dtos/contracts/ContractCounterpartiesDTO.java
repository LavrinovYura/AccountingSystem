package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class ContractCounterpartiesDTO {

    @Valid
    @NotBlank(message = "Set of Contract Counterparties is empty")
    Set< @Valid ContractCounterpartyDTO> contractCounterparties;
}
