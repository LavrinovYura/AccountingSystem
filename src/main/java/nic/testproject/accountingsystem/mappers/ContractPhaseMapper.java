package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.contracts.ContractPhaseDTO;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import org.mapstruct.*;

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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ContractPhase updateContractPhaseFromDto(@MappingTarget ContractPhase contractPhase, ContractPhaseDTO contractPhaseDTO);

}
