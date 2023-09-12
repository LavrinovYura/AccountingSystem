package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CounterpartyMapper {
    @Mapping(target = "contractCounterparties", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Counterparty updateCounterpartyFromDto(CounterpartyDTO dto, @MappingTarget Counterparty entity);
    @Mapping(target = "contractCounterparties", ignore = true)
    Counterparty counterpartyFromDTO(CounterpartyDTO counterpartyDTO);
    @Named(value = "counterpartyToDTO")
    CounterpartyDTO counterpartyToDTO(Counterparty counterparty);
    @IterableMapping(qualifiedByName = "counterpartyToDTO")
    Set<CounterpartyDTO> counterpartyPageToDTOSet(Page<Counterparty> counterparties);
}
