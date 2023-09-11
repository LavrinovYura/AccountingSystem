package nic.testproject.accountingsystem.mappers;

import nic.testproject.accountingsystem.dtos.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CounterpartyMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Counterparty updateCounterpartyFromDto(CounterpartyDTO dto, @MappingTarget Counterparty entity);

    Counterparty counterpartyFromDTO(CounterpartyDTO counterpartyDTO);

    @Named(value = "counterpartyToDTO")
    CounterpartyDTO counterpartyToDTO(Counterparty counterparty);

    @IterableMapping(qualifiedByName = "counterpartyToDTO")
    Set<CounterpartyDTO> counterpartyPageToDTOSet(Page<Counterparty> counterparties);
}
