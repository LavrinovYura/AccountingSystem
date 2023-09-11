package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.contracts.AbstractContract;
import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
import nic.testproject.accountingsystem.exceptions.ValidationException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {ContractCounterpartyMapper.class, ContractPhaseMapper.class}
)
public interface ContractMapper {

    Contract ContractFromDTO(ContractDTO contractDTO);

    @AfterMapping
    default void ContractFromDTO(@MappingTarget Contract contract){
        if (contract.getContractCounterparties() != null) {
            contract.getContractCounterparties().forEach(it -> it.setContract(contract));
        } else throw new ValidationException("Contract counterparty", "at least 1 contract counterparty needed");
        if (contract.getPhases() != null) {
            contract.getPhases().forEach( it -> it.setContract(contract));
        } else throw new ValidationException("Phases", "at least 1 phase needed");
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Contract updateContractFromDto(@MappingTarget Contract contract, AbstractContract abstractContract);

    @Named(value = "contractToDTO")
    ContractDTO contractToDTO(Contract contract);

    @IterableMapping(qualifiedByName = "contractToDTO")
    Set<ContractDTO> contractPageToDTOSet(Page<Contract> contracts);
}
