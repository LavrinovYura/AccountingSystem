package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.Util;
import nic.testproject.accountingsystem.dto.contracts.ContractDTO;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.models.contracts.details.ContractCounterparties;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import nic.testproject.accountingsystem.validation.ContractValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.ArrayList;
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
    private Util util;

    @Mock
    private ContractValidation contractValidation;

    @InjectMocks
    private ContractService contractService;

    @Captor
    private ArgumentCaptor<Contract> contractCaptor;

    public ContractServiceTests() {
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

        when(modelMapper.map(contractDTO, Contract.class)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(savedContract);
        when(modelMapper.map(savedContract, ContractDTO.class)).thenReturn(contractDTO);

        doNothing().when(util).linkContractIdToContractCounterparties(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        );
        doNothing().when(util).linkContractIdToContractPhase(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        );

        doNothing().when(contractValidation).saveValidation(any(Contract.class));

        // Act
        ContractDTO result = contractService.saveContract(contractDTO);

        // Assert
        assertSame(contractDTO, result);
        verify(contractRepository).save(contract);
        verify(contractValidation).saveValidation(contract);
        verify(util).linkContractIdToContractCounterparties(
                ArgumentMatchers.any(),
                ArgumentMatchers.any(Contract.class)
        );
        verify(util).linkContractIdToContractPhase(
                ArgumentMatchers.any(),
                ArgumentMatchers.any(Contract.class)
        );

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
        doThrow(expectedException).when(contractValidation).saveValidation(eq(contract));

        // Act & Assert
        assertThrows(ConflictException.class, () -> contractService.saveContract(contractDTO));
        verify(contractValidation).saveValidation(eq(contract));
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
    public void testUpdateContract_ExistingContract_ReturnsMappedContractDTO() {
        // Arrange
        ContractDTO contractDTO = new ContractDTO();
        String name = "Contract Name";
        Contract contract = new Contract();
        Contract savedContract = new Contract();

        Optional<Contract> optionalContract = Optional.of(contract);

        when(contractRepository.findContractByName(name)).thenReturn(optionalContract);
        when(contractRepository.save(contract)).thenReturn(savedContract);

        // Define behavior for the first invocation
        when(modelMapper.map(contract, ContractDTO.class)).thenReturn(contractDTO);

        doNothing().when(modelMapper).map(contractDTO, contract);

        doNothing().when(util).linkContractIdToContractCounterparties(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        );
        doNothing().when(util).linkContractIdToContractPhase(
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        );

        // Act
        ContractDTO result = contractService.updateContract(contractDTO, name);

        // Assert
        assertSame(contractDTO, result);
        verify(contractRepository).findContractByName(name);
        verify(contractRepository).save(contractCaptor.capture());
        verify(modelMapper).map(savedContract, ContractDTO.class);
        verify(modelMapper).map(contractDTO, contract);

        Contract capturedContract = contractCaptor.getValue();
        assertSame(contract, capturedContract);
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

        contract.setContractCounterparties(new ArrayList<>()); // Initialize the contractCounterparties list

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
