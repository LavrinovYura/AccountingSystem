package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.exceptions.ValidationException;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparty;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD
)
public abstract class ContractCounterpartyMapper {

    @Autowired
    CounterpartyRepository counterpartyRepository;

    @BeforeMapping
    protected void setCounterpartyById(@MappingTarget ContractCounterparty contractCounterparty, ContractCounterpartyDTO contractCounterpartyDTO) {
        if (contractCounterpartyDTO.getCounterpartyId() == null){
            throw new ValidationException("counterpartyId", "Counterparty id needed and can't be null");
        }

        Counterparty counterparty = counterpartyRepository.findById(contractCounterpartyDTO.getCounterpartyId())
                .orElseThrow(()-> new ResourceNotFoundException("Counterparty with id " + contractCounterpartyDTO.getCounterpartyId() + " not found"));

        contractCounterparty.setCounterparty(counterparty);
    }

    @Named(value = "contractCounterpartyFromDTO")
    public abstract ContractCounterparty contractCounterpartyFromDTO(ContractCounterpartyDTO contractCounterpartyDTO);

    @Named(value = "contractCounterpartyToDTO")
    @Mapping(target = "counterpartyId", source = "contractCounterparty.counterparty.id")
    public abstract ContractCounterpartyDTO contractCounterpartyToDTO(ContractCounterparty contractCounterparty);

    @IterableMapping(qualifiedByName = "contractCounterpartyToDTO")
    public abstract Set<ContractCounterpartyDTO> contractCounterpartyToDTOSet(Collection<ContractCounterparty> contractCounterparties);

    @IterableMapping(qualifiedByName = "contractCounterpartyFromDTO")
    public abstract Set<ContractCounterparty> contractCounterpartyFromDTOSet(Collection<ContractCounterpartyDTO> contractCounterpartiesDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract ContractCounterparty updateContractCounterpartyFromDTO(@MappingTarget ContractCounterparty contractCounterparty, ContractCounterpartyDTO contractCounterpartyDTO);
}
