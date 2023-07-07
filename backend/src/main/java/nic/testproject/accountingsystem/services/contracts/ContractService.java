package nic.testproject.accountingsystem.services.contracts;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.CounterpartyRepository;
import nic.testproject.accountingsystem.services.contracts.specs.ContractSpecifications;
import nic.testproject.accountingsystem.validation.ContractValidation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContractService {

    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;
    private final ContractValidation saveValidation;

    @Autowired
    public ContractService(ContractRepository contractRepository,
                           ModelMapper modelMapper,
                           CounterpartyRepository counterpartyRepository, ContractValidation saveValidation) {
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
        this.saveValidation = saveValidation;
    }

    public Page<Contract> getContracts(ContractDTO criteria, Pageable pageable) {
        Page<Contract> page = contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
        if (page.isEmpty())
            throw new ResourceNotFoundException();
        return page;
    }

    public ContractDTO saveContract(ContractDTO contractDTO) {

        Contract contract = modelMapper.map(contractDTO, Contract.class);

        BindingResult errors = new BeanPropertyBindingResult(contract, "contract");
        saveValidation.validate(contract, errors);

        Contract savedContract = contractRepository.save(contract);

        List<ContractPhase> contractPhase = savedContract.getPhases();
        List<ContractCounterparties> contractCounterparties = savedContract.getContractCounterparties();

        linkContractIdToContractCounterparties(contractCounterparties, savedContract);
        linkContractIdToContractPhase(contractPhase, savedContract);

        return modelMapper.map(contract, ContractDTO.class);
    }

    public ContractDTO updateContract(ContractDTO contractDTO, String name) {
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);

        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException();
        }

        Contract contract = optionalContract.get();
        modelMapper.map(contractDTO, contract);

        BindingResult errors = new BeanPropertyBindingResult(contract, "contract");
        saveValidation.validate(contract, errors);

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
            throw new ResourceNotFoundException();
        }
        contractRepository.delete(optionalContract.get());
    }

    public void deleteContract(String name) {
        Optional<Contract> optionalContract = contractRepository.findContractByName(name);
        if (!optionalContract.isPresent()) {
            throw new ResourceNotFoundException();
        }
        optionalContract.get().getContractCounterparties().forEach(it -> it.setCounterparty(null));
        contractRepository.delete(optionalContract.get());
    }

    private void linkContractIdToContractPhase(List<ContractPhase> contractPhase, Contract savedContract) {
        contractPhase.forEach(it -> it.setContract1(savedContract));
    }

    private void linkContractIdToContractCounterparties(List<ContractCounterparties> contractCounterparties, Contract savedContract) {
        contractCounterparties.forEach(it -> it.setContract2(savedContract));
    }


}
