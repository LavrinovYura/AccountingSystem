package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class ContractPhasesDTO {

    @Valid
    @NotBlank(message = "Set of contractPhases is empty")
    Set<@Valid ContractPhaseDTO> contractPhases;
}
