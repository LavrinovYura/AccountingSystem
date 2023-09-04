package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dtos.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhaseDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.mappers.ContractMapper;
import nic.testproject.accountingsystem.mappers.ContractPhaseMapper;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractPhase;
import nic.testproject.accountingsystem.repositories.contracts.ContractPhaseRepository;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import nic.testproject.accountingsystem.validation.ContractValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTests {

    @Mock
    private ContractRepository contractRepository;
    
    @Mock
    private ContractValidation contractValidation;
    
    @Mock
    private ContractMapper contractMapper;

    @Mock
    private ContractPhaseMapper contractPhaseMapper;

    @Mock
    private ContractPhaseRepository contractPhaseRepository;

    @InjectMocks
    private ContractService contractService;

    @Test
    public void testGetContracts_WhenPageIsEmpty_ThrowsResourceNotFoundException() {
        ContractCriteriaDTO criteria = new ContractCriteriaDTO();
        Pageable pageable = Pageable.ofSize(10);
        when(contractRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(Page.empty());
        assertThrows(ResourceNotFoundException.class, () -> contractService.getContracts(criteria, pageable));
        verifyNoInteractions(contractMapper);
    }

    @Test
    public void testGetContracts_WhenPageIsNotEmpty_ReturnsPageOfContracts() {
        ContractCriteriaDTO criteria = new ContractCriteriaDTO();
        Pageable pageable = Pageable.ofSize(10);
        Set<ContractDTO> contractSet = new HashSet<>();
        
        Page<Contract> expectedPage = new PageImpl<>(Collections.singletonList(new Contract()));

        when(contractRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);
        when(contractMapper.contractPageToDTOSet(expectedPage)).thenReturn(contractSet);
        contractService.getContracts(criteria, pageable);

        verifyNoMoreInteractions(contractMapper);
        verifyNoMoreInteractions(contractRepository);
    }


    @Test
    public void testSaveContract_ValidContractDTO_ReturnsMappedContractDTO() {
        ContractDTO contractDTO = new ContractDTO();
        Contract contract = new Contract();

        when(contractMapper.ContractFromDTO(contractDTO)).thenReturn(contract);
        doNothing().when(contractValidation).saveValidation(any(Contract.class));
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.contractToDTO(contract)).thenReturn(contractDTO);
        
        contractService.saveContract(contractDTO);

        verifyNoMoreInteractions(contractMapper);
        verifyNoMoreInteractions(contractRepository);
        verifyNoMoreInteractions(contractMapper);
    }
    

    @Test
    public void testUpdateContract_WhenContractNotPresent_ThrowsResourceNotFoundException() {
        ContractDTO contractDTO = new ContractDTO();
        Long id = 1L;

        when(contractRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.updateContract(contractDTO, id));
    }

    @Test
    public void testUpdateContract_ExistingContract_ReturnsMappedContractDTO() {
        ContractDTO contractDTO = new ContractDTO();
        Long id = 1L;
        Contract contract = new Contract();

        Optional<Contract> optionalContract = Optional.of(contract);

        when(contractRepository.findById(id)).thenReturn(optionalContract);
        when(contractMapper.updateContractFromDto(contract, contractDTO)).thenReturn(contract);

        contractService.updateContract(contractDTO, id);


        verifyNoMoreInteractions(contractRepository);
        verifyNoMoreInteractions(contractRepository);
    }

    @Test
    public void testDeleteContract_WhenContractNotPresent_ThrowsResourceNotFoundException() {
        Long id = 1L;

        when(contractRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> contractService.deleteContract(id));
    }

    @Test
    public void testDeleteContract_WhenContractPresent() {
        Long id = 1L;
        Contract contract = new Contract();

        Optional<Contract> optionalContract = Optional.of(contract);

        when(contractRepository.findById(id)).thenReturn(optionalContract);
        doNothing().when(contractRepository).delete(contract);
        contractService.deleteContract(id);

        verifyNoMoreInteractions(contractRepository);
    }

    @Test
    public void testAddContractPhases() {
        Long contractId = 1L;
        Set<ContractPhaseDTO> contractPhasesDTO = new HashSet<>();
        Set<ContractPhase> contractPhases = new HashSet<>();
        contractPhases.add(new ContractPhase());

        Contract contract = new Contract();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contractPhaseMapper.contractPhasesSetFromDTO(contractPhasesDTO)).thenReturn(contractPhases);
        when(contractPhaseRepository.saveAll(contractPhases)).thenReturn(new ArrayList<>());
        when(contractPhaseMapper.contractPhasesSetToDTO(contractPhases)).thenReturn(contractPhasesDTO);

        contractService.addContractPhases(contractPhasesDTO, contractId);

        verifyNoMoreInteractions(contractRepository);
        verifyNoMoreInteractions(contractPhaseMapper);
    }
}
