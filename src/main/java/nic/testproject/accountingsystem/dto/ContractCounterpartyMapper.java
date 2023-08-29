package nic.testproject.accountingsystem.dto;

import nic.testproject.accountingsystem.dto.contracts.ContractCounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparty;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class ContractCounterpartyMapper {
    @BeforeMapping
    protected void setCounterpartyById(ContractCounterpartyDTO contractCounterpartyDTO,
                                       @Context CounterpartyRepository counterpartyRepository
                                       ){
        Long id = contractCounterpartyDTO.getCounterpartyId();
        Optional<Counterparty> counterparty = counterpartyRepository.findById(id);
        if(!counterparty.isPresent()){
            throw new ResourceNotFoundException("There is no counterparty with that id");
        }
        contractCounterpartyDTO.setCounterparty(counterparty.get());
    }
    public abstract ContractCounterparty contractCounterpartyDTOtoContractCounterparty(ContractCounterpartyDTO contractCounterpartyDTO);
}
