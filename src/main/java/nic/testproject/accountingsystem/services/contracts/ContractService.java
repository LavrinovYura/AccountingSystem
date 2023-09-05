package nic.testproject.accountingsystem.services.contracts;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartyDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhaseDTO;
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

    public ContractDTO updateContract(ContractDTO contractDTO, Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));
        Contract savedContract = contractMapper.updateContractFromDto(contract,contractDTO);
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

    public void deleteContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no contract with id" + id));

        contractRepository.delete(contract);
    }
}
