package nic.testproject.accountingsystem.services;

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
import nic.testproject.accountingsystem.services.contracts.ContractService;
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

import static org.junit.Assert.assertEquals;
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
    private ContractCounterpartyRepository contractCounterpartyRepository;
    
    @Mock
    private ContractMapper contractMapper;

    @Mock
    private ContractPhaseMapper contractPhaseMapper;
    
    @Mock
    private ContractCounterpartyMapper contractCounterpartyMapper;

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
    public void testSaveContract_ReturnsMappedContractDTO() {
        ContractDTO contractDTO = new ContractDTO();
        Contract contract = new Contract();

        when(contractMapper.ContractFromDTO(contractDTO)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(contract);
        when(contractMapper.contractToDTO(contract)).thenReturn(contractDTO);
        
        contractService.saveContract(contractDTO);

        verifyNoMoreInteractions(contractMapper);
        verifyNoMoreInteractions(contractRepository);
        verifyNoMoreInteractions(contractMapper);
    }
    

    @Test
    public void testUpdateContract_ThrowsResourceNotFoundException() {
        UpdateContractDTO contractDTO = new UpdateContractDTO();
        Long id = 1L;

        when(contractRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.updateContract(contractDTO, id));
    }

    @Test
    public void testUpdateContract_ReturnsMappedContractDTO() {
        UpdateContractDTO contractDTO = new UpdateContractDTO();
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
    public void testDeleteContract_ThrowsResourceNotFoundException() {
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

    @Test
    public void testAddContractCounterparty_ContractNotFound() {
        Long contractId = 1L;
        Set<ContractCounterpartyDTO> contractCounterpartyDTO = new HashSet<>();

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.addContractCounterparty(contractCounterpartyDTO, contractId));
    }

    @Test
    public void testAddContractCounterparty() {
        Long contractId = 1L;
        Set<ContractCounterpartyDTO> contractCounterpartyDTO = new HashSet<>();
        Set<ContractCounterparty> contractCounterparties = new HashSet<>();
        contractCounterparties.add(new ContractCounterparty());

        Contract contract = new Contract();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contractCounterpartyMapper.contractCounterpartyFromDTOSet(contractCounterpartyDTO)).thenReturn(contractCounterparties);
        when(contractCounterpartyRepository.saveAll(contractCounterparties)).thenReturn(new ArrayList<>());
        when(contractCounterpartyMapper.contractCounterpartyToDTOSet(contractCounterparties)).thenReturn(contractCounterpartyDTO);

        contractService.addContractCounterparty(contractCounterpartyDTO, contractId);

        verifyNoMoreInteractions(contractRepository);
        verifyNoMoreInteractions(contractCounterpartyMapper);
    }

    @Test
    public void testAddContractPhases_ContractNotFound() {
        Long contractId = 1L;
        Set<ContractPhaseDTO> contractPhasesDTO = new HashSet<>();

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.addContractPhases(contractPhasesDTO, contractId));
    }

    @Test
    public void testUpdateContractPhase_ThrowsResourceNotFoundException() {
        ContractPhaseDTO phaseDTO = new ContractPhaseDTO();
        Long id = 1L;

        when(contractPhaseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.updateContractPhase(phaseDTO, id));

        verifyNoMoreInteractions(contractPhaseRepository);
    }

    @Test
    public void testUpdateContractCounterparty_ThrowsResourceNotFoundException() {
        ContractCounterpartyDTO counterpartyDTO = new ContractCounterpartyDTO();
        Long id = 1L;

        when(contractCounterpartyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.updateContractCounterparty(counterpartyDTO, id));

        verifyNoMoreInteractions(contractCounterpartyRepository);
    }

    @Test
    public void testUpdateContractPhase_WhenPhasePresent_ReturnsUpdatedPhaseDTO() {
        ContractPhaseDTO phaseDTO = new ContractPhaseDTO();
        Long id = 1L;
        ContractPhase phase = new ContractPhase();

        when(contractPhaseRepository.findById(id)).thenReturn(Optional.of(phase));
        when(contractPhaseMapper.updateContractPhaseFromDto(phase, phaseDTO)).thenReturn(phase);
        when(contractPhaseMapper.contractPhaseToDTO(phase)).thenReturn(phaseDTO);

        ContractPhaseDTO updatedPhaseDTO = contractService.updateContractPhase(phaseDTO, id);

        assertEquals(phaseDTO, updatedPhaseDTO);

        verifyNoMoreInteractions(contractPhaseRepository);
        verifyNoMoreInteractions(contractPhaseMapper);
    }

    @Test
    public void testUpdateContractCounterparty_WhenCounterpartyPresent_ReturnsUpdatedCounterpartyDTO() {
        ContractCounterpartyDTO counterpartyDTO = new ContractCounterpartyDTO();
        Long id = 1L;
        ContractCounterparty counterparty = new ContractCounterparty();

        
        when(contractCounterpartyRepository.findById(id)).thenReturn(Optional.of(counterparty));
        when(contractCounterpartyMapper.updateContractCounterpartyFromDTO(counterparty, counterpartyDTO)).thenReturn(counterparty);
        when(contractCounterpartyMapper.contractCounterpartyToDTO(counterparty)).thenReturn(counterpartyDTO);
        ContractCounterpartyDTO updatedCounterpartyDTO = contractService.updateContractCounterparty(counterpartyDTO, id);

        
        assertEquals(counterpartyDTO, updatedCounterpartyDTO);

        
        verifyNoMoreInteractions(contractCounterpartyRepository);
        verifyNoMoreInteractions(contractCounterpartyMapper);
    }

    @Test
    public void testDeleteContractCounterparty_ThrowsResourceNotFoundException() {
        Long id = 1L;

        when(contractCounterpartyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.deleteContractCounterparty(id));

        verifyNoMoreInteractions(contractCounterpartyRepository);
    }

    @Test
    public void testDeleteContractCounterparty_WhenCounterpartyPresent() {
        Long id = 1L;
        ContractCounterparty counterparty = new ContractCounterparty();

        Optional<ContractCounterparty> optionalCounterparty = Optional.of(counterparty);

        when(contractCounterpartyRepository.findById(id)).thenReturn(optionalCounterparty);
        doNothing().when(contractCounterpartyRepository).delete(counterparty);
        contractService.deleteContractCounterparty(id);

        verifyNoMoreInteractions(contractCounterpartyRepository);
    }

    @Test
    public void testDeletePhase_ThrowsResourceNotFoundException() {
        Long id = 1L;

        when(contractPhaseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractService.deletePhase(id));

        verifyNoMoreInteractions(contractPhaseRepository);
    }

    @Test
    public void testDeletePhase_WhenPhasePresent() {
        Long id = 1L;
        ContractPhase phase = new ContractPhase();

        Optional<ContractPhase> optionalPhase = Optional.of(phase);

        when(contractPhaseRepository.findById(id)).thenReturn(optionalPhase);
        doNothing().when(contractPhaseRepository).delete(phase);
        contractService.deletePhase(id);

        verifyNoMoreInteractions(contractPhaseRepository);
    }
}
