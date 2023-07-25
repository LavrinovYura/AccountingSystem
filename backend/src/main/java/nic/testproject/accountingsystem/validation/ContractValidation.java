package nic.testproject.accountingsystem.validation;

import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.repositories.contracts.ContractCounterpartyRepository;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ContractValidation implements Validator {

    private final ContractCounterpartyRepository contractCounterpartyRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public ContractValidation(ContractCounterpartyRepository contractCounterpartyRepository, CounterpartyRepository counterpartyRepository, ContractRepository contractRepository) {
        this.contractCounterpartyRepository = contractCounterpartyRepository;
        this.counterpartyRepository = counterpartyRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Contract.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors bindingResult) {

        Contract contract = (Contract) target;
        List<ContractCounterparties> contractCounterparties = contract.getContractCounterparties();

        if (contractRepository.existsByName(contract.getName()))
            bindingResult.rejectValue("name", "name.duplicate", "Contract with that name " + contract.getName() + " already exists");

        contractCounterparties.forEach(it -> {
            if (contractCounterpartyRepository.existsByName(it.getName())){
                bindingResult.rejectValue("contractCounterparties", "contractCounterparty.duplicate",
                        "Contract counterparty with name " + it.getName() + " already exists");
                System.out.println(contractCounterpartyRepository.existsByName(it.getName()));
            }
            if (counterpartyRepository.existsByName(it.getCounterparty().getName()))
                bindingResult.rejectValue("contractCounterparties", "counterparty.duplicate",
                        "Counterparty with name " + it.getCounterparty().getName() + " already exists");
            if (counterpartyRepository.existsByInn(it.getCounterparty().getInn()))
                bindingResult.rejectValue("contractCounterparties", "counterparty.duplicate",
                        "Counterparty with inn " + it.getCounterparty().getInn() + " already exists");
        });

        if (bindingResult.hasErrors())
            throw new ConflictException(bindingResult.toString());
    }
}
