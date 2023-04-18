package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.CounterpartySpecifications;
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

    @Autowired
    public CounterpartyService(CounterpartyRepository counterpartyRepository, ModelMapper modelMapper) {
        this.counterpartyRepository = counterpartyRepository;
        this.modelMapper = modelMapper;
    }

    public Counterparty saveCounterparty(CounterpartyDTO counterpartyDTO){
        Counterparty counterparty = modelMapper.map(counterpartyDTO,Counterparty.class);
        return counterpartyRepository.save(counterparty);
    }
    public Page<Counterparty> findCounterparties(CounterpartyDTO criteria, Pageable pageable) {
        return counterpartyRepository.findAll(CounterpartySpecifications.searchCounterparties(criteria), pageable);
    }

    public Counterparty updateCounterparty(CounterpartyDTO counterpartyDTO) {
        String name = counterpartyDTO.getName();
        Optional<Counterparty> optionalCounterparty = counterpartyRepository.findByName(name);
        if (!optionalCounterparty.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Counterparty counterparty = optionalCounterparty.get();
        return  counterpartyRepository.save(counterparty);
    }

    public void deleteCounterparty(String name) {
        Optional<Counterparty> optionalCounterparty = counterpartyRepository.findByName(name);
        if (!optionalCounterparty.isPresent()) {
            throw new ResourceNotFoundException();
        }

        Counterparty counterparty = optionalCounterparty.get();

        counterparty.getContractCounterparties().forEach(it->it.setCounterparty(null));
        counterparty.setContractCounterparties(null);

        counterpartyRepository.delete(counterparty);
    }

    public void deleteCounterpartyWithChildren(String name) {
        Optional<Counterparty> optionalCounterparty = counterpartyRepository.findByName(name);
        if (!optionalCounterparty.isPresent()) {
            throw new ResourceNotFoundException();
        }
        counterpartyRepository.delete(optionalCounterparty.get());
    }
}
