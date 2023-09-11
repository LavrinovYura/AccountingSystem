package nic.testproject.accountingsystem.dtos.contracts;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ContractPhasesDTO {

    @NotNull(message = "Set of contractPhases is empty")
    Set<@Valid ContractPhaseDTO> contractPhases;
}
