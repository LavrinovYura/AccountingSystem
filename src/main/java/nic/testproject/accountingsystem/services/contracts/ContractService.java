package nic.testproject.accountingsystem.services.contracts;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.contracts.*;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.mappers.ContractCounterpartyMapper;
import nic.testproject.accountingsystem.mappers.ContractMapper;
import nic.testproject.accountingsystem.mappers.ContractPhaseMapper;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparty;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.repositories.contracts.ContractCounterpartyRepository;
import nic.testproject.accountingsystem.repositories.contracts.ContractPhaseRepository;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.specs.ContractSpecifications;
import nic.testproject.accountingsystem.validation.ContractValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractValidation validation;
    private final ContractMapper contractMapper;
    private final ContractPhaseMapper contractPhaseMapper;
    private final ContractPhaseRepository contractPhaseRepository;
    private final ContractCounterpartyMapper contractCounterpartyMapper;
    private final ContractCounterpartyRepository contractCounterpartyRepository;

    public Set<ContractDTO> getContracts(ContractCriteriaDTO criteria, Pageable pageable) {
        Page<Contract> page = contractRepository.findAll(ContractSpecifications.searchContracts(criteria), pageable);
        if (page.isEmpty()) {
            throw new ResourceNotFoundException("There is no contracts with those criteria");
        }

        return contractMapper.contractPageToDTOSet(page);
    }

    public ContractDTO saveContract(ContractDTO contractDTO) {

        Contract contract = contractMapper.ContractFromDTO(contractDTO);

        validation.saveValidation(contract);
        Contract savedContract = contractRepository.save(contract);

        return contractMapper.contractToDTO(savedContract);
    }

    public ContractDTO updateContract(AbstractContract abstractContract, Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));
        Contract savedContract = contractMapper.updateContractFromDto(contract, abstractContract);
        return contractMapper.contractToDTO(savedContract);
    }

    public Set<ContractPhaseDTO> addContractPhases(Set<ContractPhaseDTO> contractPhasesDTO, Long id){
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));
        Set<ContractPhase> contractPhases = contractPhaseMapper.contractPhasesSetFromDTO(contractPhasesDTO);
        contractPhases.forEach(it -> it.setContract(contract));
        contractPhaseRepository.saveAll(contractPhases);
        return contractPhaseMapper.contractPhasesSetToDTO(contractPhases);
    }

    public Set<ContractCounterpartyDTO> addContractCounterparty(Set<ContractCounterpartyDTO> contractCounterpartyDTO, Long id){
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));
        Set<ContractCounterparty> contractCounterparties = contractCounterpartyMapper.contractCounterpartyFromDTOSet(contractCounterpartyDTO);
        contractCounterparties.forEach(it -> it.setContract(contract));
        contractCounterpartyRepository.saveAll(contractCounterparties);

        return contractCounterpartyMapper.contractCounterpartyToDTOSet(contractCounterparties);
    }

    public ContractPhaseDTO updateContractPhase(ContractPhaseDTO contractPhaseDTO, Long id){
       ContractPhase contractPhase = contractPhaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no phase with id" + id));

        ContractPhase updatedContractPhase = contractPhaseMapper.updateContractPhaseFromDto(contractPhase, contractPhaseDTO);
        return contractPhaseMapper.contractPhaseToDTO(updatedContractPhase);
    }

    public ContractCounterpartyDTO updateContractCounterparty(ContractCounterpartyDTO contractCounterpartyDTO, Long id){
        ContractCounterparty contractCounterparty = contractCounterpartyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contractCounterparty with id" + id));

        ContractCounterparty updatedContractCounterparty = contractCounterpartyMapper
                .updateContractCounterpartyFromDTO(contractCounterparty,contractCounterpartyDTO);
        return contractCounterpartyMapper.contractCounterpartyToDTO(updatedContractCounterparty);
    }

    public void deleteContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));

        contractRepository.delete(contract);
    }

    public void deleteContractCounterparty(Long id) {
        ContractCounterparty contractCounterparty = contractCounterpartyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));

        contractCounterpartyRepository.delete(contractCounterparty);
    }

    public void deletePhase(Long id) {
        ContractPhase contractPhase = contractPhaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));

        contractPhaseRepository.delete(contractPhase);
    }
}
