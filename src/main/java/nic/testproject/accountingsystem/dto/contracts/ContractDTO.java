package nic.testproject.accountingsystem.dto.contracts;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractDTO extends AbstractContract{

    private long id;

    @NotBlank
    private Set<ContractPhaseDTO> phases;

    @NotBlank
    private Set<ContractCounterpartiesDTO> contractCounterparties;
}
