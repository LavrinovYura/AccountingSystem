package nic.testproject.accountingsystem.services.contracts;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dto.ContractMapper;
import nic.testproject.accountingsystem.dto.contracts.ContractCounterpartyDTO;
import nic.testproject.accountingsystem.dto.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparty;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.ContractSpecifications;
import nic.testproject.accountingsystem.validation.ContractValidation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;
    private final ContractValidation validation;
    private final ContractMapper contractMapper;

    public Page<Contract> getContracts(ContractCriteriaDTO criteria, Pageable pageable) {
        Page<Contract> page = contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("There is no contracts with those criteria");
        }
        return page;
    }

    public ContractDTO saveContract(ContractDTO contractDTO) {

        Contract contract = contractMapper.mapToContract(contractDTO);

        validation.saveValidation(contract);

        System.out.println(contract.getContractCounterparties());

        Contract savedContract = saveContractTest(contract);

        return modelMapper.map(savedContract, ContractDTO.class);
    }
    @Transactional
    public Contract saveContractTest(Contract contract) {
        return contractRepository.save(contract);
    }

    public ContractDTO updateContract(ContractDTO contractDTO, String name) {
        Contract contract = contractRepository.findContractByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with name" + name));

        validation.updateValidation(contract, contractDTO.getName());

        modelMapper.map(contractDTO, contract);

        Contract savedContract = contractRepository.save(contract);

        return modelMapper.map(savedContract, ContractDTO.class);
    }

    public void deleteContract(String name) {
        Contract contract = contractRepository.findContractByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with name" + name));

        contractRepository.delete(contract);
    }
}
