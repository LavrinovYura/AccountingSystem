package nic.testproject.accountingsystem.dto;

import nic.testproject.accountingsystem.dto.contracts.ContractPhaseDTO;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractPhaseMapper {
    ContractPhase contractPhaseDTOtoContractPhase(ContractPhaseDTO contractPhaseDTO);

}
