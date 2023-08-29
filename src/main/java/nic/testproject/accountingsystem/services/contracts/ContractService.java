package nic.testproject.accountingsystem.services.contracts;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dto.contracts.ContractCounterpartiesDTO;
import nic.testproject.accountingsystem.dto.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
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
    private final CounterpartyRepository counterpartyRepository;
    private final ModelMapper modelMapper;
    private final ContractValidation validation;

    public Page<Contract> getContracts(ContractCriteriaDTO criteria, Pageable pageable) {
        Page<Contract> page = contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("There is no contracts with those criteria");
        }
        return page;
    }

    public ContractDTO saveContract(ContractDTO contractDTO) {

        Set<ContractCounterpartiesDTO> contractCounterpartiesDTOS = contractDTO.getContractCounterparties();
        contractCounterpartiesDTOS.forEach(it -> {
                it.getCounterparty().setInn("111111");
                it.getCounterparty().setAddress("address");
        });

        System.out.println(contractDTO);
        contractDTO.setContractCounterparties(contractCounterpartiesDTOS);

        Contract contract = modelMapper.map(contractDTO, Contract.class);

        System.out.println(contract);
        contract.getContractCounterparties().forEach(it -> {
            Counterparty counterparty = counterpartyRepository
                    .findByName(it.getCounterparty().getName())
                    .orElse(null);
            it.setCounterparty(counterparty);
        });

        validation.saveValidation(contract);

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
