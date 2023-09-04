package nic.testproject.accountingsystem.validation;

import nic.testproject.accountingsystem.exceptions.ValidationException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContractValidation {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractValidation(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public void saveValidation(Contract contract)  {

        if (contractRepository.existsByName(contract.getName())) {
            throw new ValidationException("name", "A contract with the same name already exists");
        }

    }
}
