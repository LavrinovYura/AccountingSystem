package nic.testproject.accountingsystem.services.contracts;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.mappers.CounterpartyMapper;
import nic.testproject.accountingsystem.dtos.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.CounterpartySpecifications;
import nic.testproject.accountingsystem.validation.CounterpartyValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CounterpartyService {

    private final CounterpartyRepository counterpartyRepository;
    private final CounterpartyValidation validation;
    private final CounterpartyMapper counterpartyMapper;

    public CounterpartyDTO saveCounterparty(CounterpartyDTO counterpartyDTO) {

        Counterparty counterparty = counterpartyMapper.counterpartyFromDTO(counterpartyDTO);
        validation.saveValidation(counterparty);
        counterpartyRepository.save(counterparty);

        return counterpartyMapper.counterpartyToDTO(counterparty);
    }

    public Set<CounterpartyDTO> findCounterparties(CounterpartyDTO criteria, Pageable pageable) {
        Page<Counterparty> counterparties = counterpartyRepository.findAll(CounterpartySpecifications.searchCounterparties(criteria), pageable);

        if (counterparties.isEmpty()) {
            throw new ResourceNotFoundException("There is no counterparties");
        }
        return counterpartyMapper.counterpartyPageToDTOSet(counterparties);
    }

    public CounterpartyDTO updateCounterparty(CounterpartyDTO counterpartyDTO, Long id) {
        Counterparty counterparty = counterpartyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no counterparty with that id"));

        validation.updateValidation(counterparty, counterpartyDTO);

        Counterparty updatedCounterparty = counterpartyMapper.updateCounterpartyFromDto(counterpartyDTO, counterparty);

        return counterpartyMapper.counterpartyToDTO(updatedCounterparty);
    }

    public void deleteCounterparty(Long id) {
        Counterparty counterparty = counterpartyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no counterparty with id" + id));

        counterpartyRepository.delete(counterparty);
    }
}
