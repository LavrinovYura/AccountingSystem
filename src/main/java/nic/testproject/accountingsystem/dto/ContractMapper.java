package nic.testproject.accountingsystem.dto;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        uses = {ContractCounterpartyMapper.class,ContractPhaseMapper.class}
)
public interface ContractMapper {
    Contract mapToContract(ContractDTO contractDTO);
    @AfterMapping
    default void mapToContract(@MappingTarget Contract contract, ContractDTO contractDto){
        if (contract.getContractCounterparties() != null)
            contract.getContractCounterparties().forEach(it -> it.setContract(contract));
        if (contract.getPhases() != null) {
            contract.getPhases().forEach( it -> it.setContract(contract));
        }
    }
}
