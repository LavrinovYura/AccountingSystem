package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Valid
public class ContractDTO extends AbstractContract{

    private Long id;

    @NotBlank
    private Set<@Valid ContractPhaseDTO> phases;

    @NotBlank
    private Set<@Valid ContractCounterpartyDTO> contractCounterparties;
}
