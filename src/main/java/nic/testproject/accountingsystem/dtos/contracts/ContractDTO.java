package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractDTO extends AbstractContract{

    private Long id;

    private Set<@Valid ContractPhaseDTO> phases;

    private Set<@Valid ContractCounterpartyDTO> contractCounterparties;
}
