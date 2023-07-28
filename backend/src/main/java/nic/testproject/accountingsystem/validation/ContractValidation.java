package nic.testproject.accountingsystem.validation;

import nic.testproject.accountingsystem.exceptions.ValidationException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractValidation {

    private final CounterpartyRepository counterpartyRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public ContractValidation(CounterpartyRepository counterpartyRepository, ContractRepository contractRepository) {
        this.counterpartyRepository = counterpartyRepository;
        this.contractRepository = contractRepository;
    }

    public void saveValidation(Contract contract)  {

        List<ContractCounterparties> contractCounterparties = contract.getContractCounterparties();

        if (contractRepository.existsByName(contract.getName()))
            throw new ValidationException("name", "A contract with the same name already exists");

        contractCounterparties.forEach(it -> {
            if (counterpartyRepository.existsByName(it.getCounterparty().getName()))
                throw new ValidationException("name", "A counterparty with the same name already exists");

            if (counterpartyRepository.existsByInn(it.getCounterparty().getInn()))
                throw new ValidationException("inn", "A counterparty with the same INN already exists");

        });

    }

    public void updateValidation(Contract contract, String contractDTO) {
        if (!contract.getName().equals(contractDTO) && contractRepository.existsByName(contract.getName()))
            throw new ValidationException("name", "A contract with the same name already exists");
    }


}
