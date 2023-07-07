package nic.testproject.accountingsystem.validation;

import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CounterpartyValidation implements Validator {
    private final CounterpartyRepository counterpartyRepository;

    @Autowired
    public CounterpartyValidation(CounterpartyRepository counterpartyRepository) {
        this.counterpartyRepository = counterpartyRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Contract.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors bindingResult) {

        Counterparty counterparty = (Counterparty) target;

        if (counterpartyRepository.existsByName(counterparty.getName()))
            bindingResult.rejectValue("counterparty", "counterparty.duplicate",
                    "Counterparty with name " + counterparty.getName() + " already exists");
        if (counterpartyRepository.existsByInn(counterparty.getInn()))
            bindingResult.rejectValue("counterparty", "counterparty.duplicate",
                    "Counterparty with inn " + counterparty.getInn() + " already exists");

        if (bindingResult.hasErrors())
            throw new ConflictException(bindingResult.toString());
    }
}
