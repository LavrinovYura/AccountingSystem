//package accountingsystem.services;
//
//import nic.testproject.accountingsystem.dto.report.AllContracts;
//import nic.testproject.accountingsystem.exceptions.ConflictException;
//import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
//import nic.testproject.accountingsystem.models.contracts.ContractType;
//import nic.testproject.accountingsystem.repositories.contracts.ContractCounterpartyRepository;
//import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
//import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
//import nic.testproject.accountingsystem.repositories.contracts.projections.ContractPhaseProjection;
//import nic.testproject.accountingsystem.repositories.contracts.projections.ContractProjection;
//import nic.testproject.accountingsystem.repositories.contracts.projections.ContractWithPhasesProjection;
//import nic.testproject.accountingsystem.services.reports.ContractReportService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ContractReportServiceTests {
//
//    @Mock
//    private ContractRepository contractRepository;
//
//    @Mock
//    private ContractCounterpartyRepository contractCounterpartyRepository;
//
//    @InjectMocks
//    private ContractReportService contractReportService;
//
//    @Test
//    public void getAllContractsByPeriod_WithValidDates_ReturnsAllContracts() {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 12, 31);
//        List<ContractProjection> contracts = createMockContractProjections();
//        List<ContractCounterpartiesProjection> contractCounterparties = createMockContractCounterpartiesProjections();
//        when(contractRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(contracts);
//        when(contractCounterpartyRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(contractCounterparties);
//
//        // Act
//        AllContracts result = contractReportService.getAllContractsByPeriod(startDate, endDate);
//
//        // Assert
//        assertEquals(contracts, result.getContracts());
//        assertEquals(contractCounterparties, result.getContractCounterparties());
//        verify(contractRepository).findByPlannedStartDateBetween(startDate, endDate);
//        verify(contractCounterpartyRepository).findByPlannedStartDateBetween(startDate, endDate);
//    }
//
//    @Test
//    public void getAllContractsByPeriod_WithEndDateBeforeStartDate_ThrowsConflictException() {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2022, 12, 31);
//
//        // Act & Assert
//        assertThrows(ConflictException.class, () -> contractReportService.getAllContractsByPeriod(startDate, endDate));
//        verifyNoInteractions(contractRepository);
//        verifyNoInteractions(contractCounterpartyRepository);
//    }
//
//    @Test
//    public void getAllContractsByPeriod_WithEmptyContracts_ThrowsResourceNotFoundException() {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 12, 31);
//        when(contractRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());
//
//        // Act & Assert
//        assertThrows(ResourceNotFoundException.class, () -> contractReportService.getAllContractsByPeriod(startDate, endDate));
//        verify(contractRepository).findByPlannedStartDateBetween(startDate, endDate);
//    }
//
//    @Test
//    public void getAllContractsByPeriod_WithEmptyContractCounterparties_ThrowsResourceNotFoundException() {
//        // Arrange
//        LocalDate startDate = LocalDate.of(2023, 1, 1);
//        LocalDate endDate = LocalDate.of(2023, 12, 31);
//        List<ContractProjection> contracts = createMockContractProjections();
//        when(contractRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(contracts);
//        when(contractCounterpartyRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());
//
//        // Act & Assert
//        assertThrows(ResourceNotFoundException.class, () -> contractReportService.getAllContractsByPeriod(startDate, endDate));
//        verify(contractRepository).findByPlannedStartDateBetween(startDate, endDate);
//        verify(contractCounterpartyRepository).findByPlannedStartDateBetween(startDate, endDate);
//    }
//
//    @Test
//    public void getAllPhasesByContract_WithValidContractName_ReturnsContractPhases() {
//        // Arrange
//        String contractName = "Test Contract";
//        ContractWithPhasesProjection contract = createMockContractWithPhasesProjection();
//        when(contractRepository.findByName(contractName)).thenReturn(Optional.of(contract));
//
//        // Act
//        List<ContractPhaseProjection> result = contractReportService.getAllPhasesByContract(contractName);
//
//        // Assert
//        assertEquals(contract.getPhases(), result);
//        verify(contractRepository).findByName(contractName);
//    }
//
//    @Test
//    public void getAllPhasesByContract_WithInvalidContractName_ThrowsResourceNotFoundException() {
//        // Arrange
//        String contractName = "Nonexistent Contract";
//        when(contractRepository.findByName(contractName)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ResourceNotFoundException.class, () -> contractReportService.getAllPhasesByContract(contractName));
//        verify(contractRepository).findByName(contractName);
//    }
//
//    private ContractWithPhasesProjection createMockContractWithPhasesProjection() {
//        return () -> null;
//    }
//
//    private List<ContractProjection> createMockContractProjections() {
//        ContractProjection contracts = new ContractProjection() {
//            @Override
//            public String getName() {
//                return null;
//            }
//
//            @Override
//            public ContractType getType() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getPlannedStartDate() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getPlannedEndDate() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getActualStartDate() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getActualEndDate() {
//                return null;
//            }
//
//            @Override
//            public Double getAmount() {
//                return null;
//            }
//        };
//        return Collections.singletonList(contracts);
//    }
//
//    private List<ContractCounterpartiesProjection> createMockContractCounterpartiesProjections() {
//        ContractCounterpartiesProjection contracts = new ContractCounterpartiesProjection() {
//            @Override
//            public String getName() {
//                return null;
//            }
//
//            @Override
//            public Double getAmount() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getPlannedStartDate() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getPlannedEndDate() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getActualStartDate() {
//                return null;
//            }
//
//            @Override
//            public LocalDate getActualEndDate() {
//                return null;
//            }
//
//            @Override
//            public ContractType getType() {
//                return null;
//            }
//
//            @Override
//            public ContractProjection getContract2() {
//                return null;
//            }
//        };
//        return Collections.singletonList(contracts);
//    }
//
//}
