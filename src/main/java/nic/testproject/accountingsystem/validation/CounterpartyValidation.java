package nic.testproject.accountingsystem.validation;

import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ValidationException;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CounterpartyValidation {
    private final CounterpartyRepository counterpartyRepository;

    @Autowired
    public CounterpartyValidation(CounterpartyRepository counterpartyRepository) {
        this.counterpartyRepository = counterpartyRepository;
    }

    public void saveValidation(Counterparty counterparty) throws ValidationException {

        if (counterpartyRepository.existsByName(counterparty.getName())) {
            throw new ValidationException("name", "A counterparty with the same name already exists");
        }

        if (counterpartyRepository.existsByInn(counterparty.getInn())) {
            throw new ValidationException("inn", "A counterparty with the same INN already exists");
        }
    }

    public void updateValidation(Counterparty counterparty, CounterpartyDTO counterpartyDTO) throws ValidationException {

        if (!counterparty.getName().equals(counterpartyDTO.getName()) && counterpartyRepository.existsByName(counterpartyDTO.getName())) {
            throw new ValidationException("name", "A counterparty with the same name already exists");
        }

        if (!counterparty.getInn().equals(counterpartyDTO.getInn()) && counterpartyRepository.existsByInn(counterpartyDTO.getInn())) {
            throw new ValidationException("inn", "A counterparty with the same INN already exists");
        }
    }

}

