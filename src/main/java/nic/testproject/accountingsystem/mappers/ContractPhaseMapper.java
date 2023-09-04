package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.contracts.ContractPhaseDTO;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ContractPhaseMapper {

    @Named(value = "contractPhaseFromDTO")
    ContractPhase contractPhaseFromDTO(ContractPhaseDTO contractPhaseDTO);

    @Named(value = "contractPhaseToDTO")
    ContractPhaseDTO contractPhaseToDTO(ContractPhase contractPhase);

    @IterableMapping(qualifiedByName = "contractPhaseFromDTO")
    Set<ContractPhase> contractPhasesSetFromDTO(Set<ContractPhaseDTO> contractPhaseDTOSet);

    @IterableMapping(qualifiedByName = "contractPhaseToDTO")
    Set<ContractPhaseDTO> contractPhasesSetToDTO(Set<ContractPhase> contractPhaseSet);
}
