package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Valid
public class ContractDTO extends AbstractContract{

    private Long id;

    @NotNull(message = "Min 1 phase needed")
    private Set<@Valid ContractPhaseDTO> phases;

    @NotNull(message = "Min 1 contract with counterparty needed")
    private Set<@Valid ContractCounterpartyDTO> contractCounterparties;
}
