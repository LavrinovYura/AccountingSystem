package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {ContractCounterpartyMapper.class, ContractPhaseMapper.class}
)
public interface ContractMapper {

    Contract ContractFromDTO(ContractDTO contractDTO);

    @AfterMapping
    default void ContractFromDTO(@MappingTarget Contract contract){
        if (contract.getContractCounterparties() != null)
            contract.getContractCounterparties().forEach(it -> it.setContract(contract));
        if (contract.getPhases() != null) {
            contract.getPhases().forEach( it -> it.setContract(contract));
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Contract updateContractFromDto(@MappingTarget Contract contract, ContractDTO contractDTO);

    @Named(value = "contractToDTO")
    ContractDTO contractToDTO(Contract contract);

    @IterableMapping(qualifiedByName = "contractToDTO")
    Set<ContractDTO> contractPageToDTOSet(Page<Contract> contracts);
}
