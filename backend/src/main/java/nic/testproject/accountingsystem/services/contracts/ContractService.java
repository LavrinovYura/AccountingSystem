package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.dto.contracts.update.UpdateContractDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.ContractSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContractService {

    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ContractService(ContractRepository contractRepository,
                           ModelMapper modelMapper,
                           CounterpartyRepository counterpartyRepository) {
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
    }

    public Page<Contract> getContracts(ContractDTO criteria, Pageable pageable) {
        return contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
    }

    public Contract saveContract(ContractDTO contractDTO) {

        Contract contract = modelMapper.map(contractDTO, Contract.class);
        Contract savedContract = contractRepository.save(contract);

        List<ContractPhase> contractPhase = savedContract.getPhases();
        List<ContractCounterparties> contractCounterparties = savedContract.getContractCounterparties();

        linkContractIdToContractCounterparties(contractCounterparties, savedContract);
        linkContractIdToContractPhase(contractPhase, savedContract);

        return savedContract;
    }

    public ContractDTO updateContract(UpdateContractDTO updateContractDTO) {
        String name = updateContractDTO.getName();
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);
        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException("Contract not found with id: " + name);
        }

        Contract contract = optionalContract.get();
        modelMapper.map(updateContractDTO, contract);
        Contract savedContract = contractRepository.saveAndFlush(contract);

        List<ContractPhase> contractPhase = savedContract.getPhases();
        List<ContractCounterparties> contractCounterparties = savedContract.getContractCounterparties();

        linkContractIdToContractCounterparties(contractCounterparties, savedContract);
        linkContractIdToContractPhase(contractPhase, savedContract);

        return modelMapper.map(savedContract, ContractDTO.class);
    }

    public void deleteContractWithChildren(String name) {
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);
        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException("Counterparty not found with id: " + name);
        }
        contractRepository.delete(optionalContract.get());
    }

    public void deleteContract(String name) {
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);
        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException("Counterparty not found with id: " + name);
        }
        optionalContract.get().getContractCounterparties().forEach(it->it.setCounterparty(null));
        contractRepository.delete(optionalContract.get());
    }

    private void linkContractIdToContractPhase(List<ContractPhase> contractPhase, Contract savedContract) {
        contractPhase.forEach(it -> it.setContract1(savedContract));
    }

    private void linkContractIdToContractCounterparties(List<ContractCounterparties> contractCounterparties, Contract savedContract) {
        contractCounterparties.forEach(it -> it.setContract2(savedContract));
    }


}
