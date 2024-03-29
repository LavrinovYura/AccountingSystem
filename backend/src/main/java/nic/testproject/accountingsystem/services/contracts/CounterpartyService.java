package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.CounterpartySpecifications;
import nic.testproject.accountingsystem.validation.CounterpartyValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CounterpartyService {

    private final CounterpartyRepository counterpartyRepository;
    private final ModelMapper modelMapper;
    private final CounterpartyValidation validation;

    @Autowired
    public CounterpartyService(CounterpartyRepository counterpartyRepository, ModelMapper modelMapper, CounterpartyValidation validation) {
        this.counterpartyRepository = counterpartyRepository;
        this.modelMapper = modelMapper;
        this.validation = validation;
    }

    public CounterpartyDTO saveCounterparty(CounterpartyDTO counterpartyDTO) {
        Counterparty counterparty = modelMapper.map(counterpartyDTO, Counterparty.class);
        validation.saveValidation(counterparty);
        return modelMapper.map(counterpartyRepository.save(counterparty), CounterpartyDTO.class);

    }

    public Page<Counterparty> findCounterparties(CounterpartyDTO criteria, Pageable pageable) {
        Page<Counterparty> counterparties= counterpartyRepository.findAll(CounterpartySpecifications.searchCounterparties(criteria), pageable);
        if(counterparties.isEmpty())
            throw new ResourceNotFoundException("There is no counterparties");

        return counterparties;
    }

    public CounterpartyDTO updateCounterparty(CounterpartyDTO counterpartyDTO, String name)   {
        Optional<Counterparty> optionalCounterparty = counterpartyRepository.findByName(name);

        if (!optionalCounterparty.isPresent()) {
            throw new ResourceNotFoundException("There is no counterparty with name" + name);
        }

        Counterparty counterparty = optionalCounterparty.get();

        validation.updateValidation(counterparty, counterpartyDTO);

        modelMapper.map(counterpartyDTO, counterparty);
        Counterparty updatedCounterparty = counterpartyRepository.save(counterparty);

        return modelMapper.map(updatedCounterparty, CounterpartyDTO.class);
    }

    public void deleteCounterparty(String name) {
        Optional<Counterparty> optionalCounterparty = counterpartyRepository.findByName(name);
        if (!optionalCounterparty.isPresent()) {
            throw new ResourceNotFoundException("There is no counterparty with name" + name);
        }

        Counterparty counterparty = optionalCounterparty.get();

        counterparty.getContractCounterparties().forEach(it -> it.setCounterparty(null));
        counterparty.setContractCounterparties(null);

        counterpartyRepository.delete(counterparty);
    }

    public void deleteCounterpartyWithChildren(String name) {
        Optional<Counterparty> optionalCounterparty = counterpartyRepository.findByName(name);
        if (!optionalCounterparty.isPresent()) {
            throw new ResourceNotFoundException("There is no counterparty with name" + name);
        }
        counterpartyRepository.delete(optionalCounterparty.get());
    }
}
