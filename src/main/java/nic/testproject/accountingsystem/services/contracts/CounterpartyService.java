package nic.testproject.accountingsystem.services.contracts;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dto.CounterpartyMapper;
import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.CounterpartySpecifications;
import nic.testproject.accountingsystem.validation.CounterpartyValidation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterpartyService {

    private final CounterpartyRepository counterpartyRepository;
    private final ModelMapper modelMapper;
    private final CounterpartyValidation validation;
    private final CounterpartyMapper counterpartyMapper;

    public CounterpartyDTO saveCounterparty(CounterpartyDTO counterpartyDTO) {

        Counterparty counterparty = modelMapper.map(counterpartyDTO, Counterparty.class);
        validation.saveValidation(counterparty);

        return modelMapper.map(counterpartyRepository.save(counterparty), CounterpartyDTO.class);
    }

    public Page<Counterparty> findCounterparties(CounterpartyDTO criteria, Pageable pageable) {
        Page<Counterparty> counterparties = counterpartyRepository.findAll(CounterpartySpecifications.searchCounterparties(criteria), pageable);

        if (counterparties.isEmpty()) {
            throw new ResourceNotFoundException("There is no counterparties");
        }

        return counterparties;
    }

    public CounterpartyDTO updateCounterparty(CounterpartyDTO counterpartyDTO, Long id) {
        Counterparty counterparty = counterpartyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no counterparty with that id"));

        validation.updateValidation(counterparty, counterpartyDTO);

        Counterparty updatedCounterparty = counterpartyMapper.updateCounterpartyFromDto(counterpartyDTO, counterparty);

        return modelMapper.map(updatedCounterparty, CounterpartyDTO.class);
    }

    public void deleteCounterparty(Long id) {
        Counterparty counterparty = counterpartyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no counterparty with id" + id));

        counterpartyRepository.delete(counterparty);
    }
}
