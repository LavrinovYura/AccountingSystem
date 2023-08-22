package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.Util;
import nic.testproject.accountingsystem.dto.contracts.ContractCounterpartiesDTO;
import nic.testproject.accountingsystem.dto.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.dto.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.ContractSpecifications;
import nic.testproject.accountingsystem.validation.ContractValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    private final CounterpartyRepository counterpartyRepository;
    private final ModelMapper modelMapper;
    private final ContractValidation validation;
    private final Util util;

    @Autowired
    public ContractService(ContractRepository contractRepository, CounterpartyRepository counterpartyRepository, ModelMapper modelMapper, ContractValidation validation, Util util) {
        this.contractRepository = contractRepository;
        this.counterpartyRepository = counterpartyRepository;
        this.modelMapper = modelMapper;
        this.validation = validation;
        this.util = util;
    }

    public Page<Contract> getContracts(ContractCriteriaDTO criteria, Pageable pageable) {
        Page<Contract> page = contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
        if (page.isEmpty()) throw new ResourceNotFoundException("There is no contracts with those criteria");
        return page;
    }

    @Transactional
    public ContractDTO saveContract(ContractDTO contractDTO) {

        List<ContractCounterpartiesDTO> contractCounterpartiesDTOS = contractDTO.getContractCounterparties();
        contractCounterpartiesDTOS.forEach(it -> {
            it.getCounterparty().setInn("111111");
            it.getCounterparty().setAddress("address");
        });

        Contract contract = modelMapper.map(contractDTO, Contract.class);

        contract.getContractCounterparties().forEach(it -> {
            Counterparty counterparty = counterpartyRepository
                    .findByName(it.getCounterparty().getName())
                    .orElse(null);
            it.setCounterparty(counterparty);
        });

        validation.saveValidation(contract);

        Contract savedContract = contractRepository.save(contract);

        List<ContractPhase> contractPhase = savedContract.getPhases();
        List<ContractCounterparties> contractCounterparties = savedContract.getContractCounterparties();

        util.linkContractIdToContractCounterparties(contractCounterparties, savedContract);
        util.linkContractIdToContractPhase(contractPhase, savedContract);

        return modelMapper.map(contract, ContractDTO.class);
    }

    public ContractDTO updateContract(ContractDTO contractDTO, String name) {
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);

        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException("There is no contract with name" + name);
        }

        Contract contract = optionalContract.get();

        validation.updateValidation(contract, contractDTO.getName());

        modelMapper.map(contractDTO, contract);

        Contract savedContract = contractRepository.save(contract);

        List<ContractPhase> contractPhase = savedContract.getPhases();
        List<ContractCounterparties> contractCounterparties = savedContract.getContractCounterparties();

        util.linkContractIdToContractCounterparties(contractCounterparties, savedContract);
        util.linkContractIdToContractPhase(contractPhase, savedContract);

        return modelMapper.map(savedContract, ContractDTO.class);
    }

    public void deleteContractWithChildren(String name) {
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);
        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException("There is no contract with name" + name);
        }
        contractRepository.delete(optionalContract.get());
    }

    public void deleteContract(String name) {
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);
        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException("There is no contract with name" + name);
        }
        optionalContract.get().getContractCounterparties().forEach(it -> it.setCounterparty(null));
        contractRepository.delete(optionalContract.get());
    }
}
