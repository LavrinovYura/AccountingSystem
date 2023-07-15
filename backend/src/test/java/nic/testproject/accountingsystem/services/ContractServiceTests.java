package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import nic.testproject.accountingsystem.services.contracts.specs.ContractSpecifications;
import nic.testproject.accountingsystem.validation.ContractValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTests {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ContractSpecifications contractSpecifications;

    @Mock
    private ContractValidation contractValidation;

    @InjectMocks
    private ContractService contractService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        contractService = new ContractService(contractRepository, modelMapper, contractValidation);
    }

    @Test
    public void testGetContracts_WhenPageIsEmpty_ThrowsResourceNotFoundException() {
        // Arrange
        ContractDTO criteria = new ContractDTO();
        Pageable pageable = Pageable.ofSize(10);

        when(contractRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(Page.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> contractService.getContracts(criteria, pageable));
    }

    @Test
    public void testGetContracts_WhenPageIsNotEmpty_ReturnsPageOfContracts() {
        // Arrange
        ContractDTO criteria = new ContractDTO();
        Pageable pageable = Pageable.ofSize(10);
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        List<Contract> contractList = Arrays.asList(contract1, contract2);
        Page<Contract> expectedPage = new PageImpl<>(contractList);

        when(contractRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Contract> resultPage = contractService.getContracts(criteria, pageable);

        // Assert
        assertEquals(expectedPage, resultPage);
    }


    @Test
    public void testSaveContract_ValidContractDTO_ReturnsMappedContractDTO() {
        // Arrange
        ContractDTO contractDTO = new ContractDTO();
        Contract contract = new Contract();
        Contract savedContract = new Contract();

        BindingResult errors = new BeanPropertyBindingResult(contract, "contract");

        when(modelMapper.map(contractDTO, Contract.class)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(savedContract);
        when(modelMapper.map(savedContract, ContractDTO.class)).thenReturn(contractDTO);
        doNothing().when(contractService).linkContractIdToContractCounterparties(anyList(), any(Contract.class));
        doNothing().when(contractService).linkContractIdToContractPhase(anyList(), any(Contract.class));
        doNothing().when(contractValidation).validate(any(Contract.class), any(BindingResult.class));

        // Act
        ContractDTO result = contractService.saveContract(contractDTO);

        // Assert
        assertSame(contractDTO, result);
        verify(contractRepository).save(contract);
        verify(contractValidation).validate(contract, errors);
        verify(contractService).linkContractIdToContractCounterparties(anyList(), eq(savedContract));
        verify(contractService).linkContractIdToContractPhase(anyList(), eq(savedContract));
    }




    @Test
    void saveContract_InvalidContractDTO_ConflictExceptionThrown() {
        // Arrange
        ContractDTO contractDTO = new ContractDTO();
        Contract contract = new Contract();
        Errors bindingResult = new BeanPropertyBindingResult(contract, "contract");
        bindingResult.rejectValue("name", "name.duplicate", "Contract with that name already exists");
        ConflictException expectedException = new ConflictException(bindingResult.toString());

        when(modelMapper.map(contractDTO, Contract.class)).thenReturn(contract);
        doThrow(expectedException).when(contractValidation).validate(eq(contract), any(Errors.class));

        // Act & Assert
        assertThrows(ConflictException.class, () -> contractService.saveContract(contractDTO));
        verify(contractValidation).validate(eq(contract), any(Errors.class));
        verifyNoInteractions(contractRepository, modelMapper);
    }



    @Test
    public void testUpdateContract_WhenContractNotPresent_ThrowsResourceNotFoundException() {
        // Arrange
        ContractDTO contractDTO = new ContractDTO();
        String name = "Contract Name";

        when(contractRepository.findContractByName(name)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> contractService.updateContract(contractDTO, name));
    }

    @Test
    public void testUpdateContract_ValidContractDTO_ReturnsMappedContractDTO() {
        // Arrange
        ContractDTO contractDTO = new ContractDTO();
        String name = "Contract Name";
        Contract contract = new Contract();
        Contract savedContract = new Contract();

        Optional<Contract> optionalContract = Optional.of(contract);
        BindingResult errors = new BeanPropertyBindingResult(contract, "contract");

        when(contractRepository.findContractByName(name)).thenReturn(optionalContract);
        when(contractRepository.saveAndFlush(contract)).thenReturn(savedContract);
        when(modelMapper.map(savedContract, ContractDTO.class)).thenReturn(contractDTO);
        //when(saveValidation.validate(contract, errors)).thenReturn(errors);

        // Act
        ContractDTO result = contractService.updateContract(contractDTO, name);

        // Assert
        assertSame(contractDTO, result);
        verify(contractRepository).saveAndFlush(contract);
        verify(contractValidation).validate(contract, errors);
    }

    @Test
    public void testDeleteContractWithChildren_WhenContractNotPresent_ThrowsResourceNotFoundException() {
        // Arrange
        String name = "Contract Name";

        when(contractRepository.findContractByName(name)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> contractService.deleteContractWithChildren(name));
    }

    @Test
    public void testDeleteContractWithChildren_WhenContractPresent_DeletesContract() {
        // Arrange
        String name = "Contract Name";
        Contract contract = new Contract();

        Optional<Contract> optionalContract = Optional.of(contract);

        when(contractRepository.findContractByName(name)).thenReturn(optionalContract);

        // Act
        contractService.deleteContractWithChildren(name);

        // Assert
        verify(contractRepository).delete(contract);
    }

    @Test
    public void testDeleteContract_WhenContractNotPresent_ThrowsResourceNotFoundException() {
        // Arrange
        String name = "Contract Name";

        when(contractRepository.findContractByName(name)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> contractService.deleteContract(name));
    }

    @Test
    public void testDeleteContract_WhenContractPresent_DeletesContractAndUnlinksCounterparties() {
        // Arrange
        String name = "Contract Name";
        Contract contract = new Contract();
        ContractCounterparties contractCounterparty1 = new ContractCounterparties();
        ContractCounterparties contractCounterparty2 = new ContractCounterparties();

        Optional<Contract> optionalContract = Optional.of(contract);

        contract.getContractCounterparties().add(contractCounterparty1);
        contract.getContractCounterparties().add(contractCounterparty2);

        when(contractRepository.findContractByName(name)).thenReturn(optionalContract);

        // Act
        contractService.deleteContract(name);

        // Assert
        assertNull(contractCounterparty1.getContract2());
        assertNull(contractCounterparty2.getContract2());
        verify(contractRepository).delete(contract);
    }
}
