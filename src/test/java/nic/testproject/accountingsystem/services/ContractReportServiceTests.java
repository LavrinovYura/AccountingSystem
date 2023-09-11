package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dtos.report.AllContracts;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.contracts.ContractType;
import nic.testproject.accountingsystem.repositories.contracts.ContractCounterpartyRepository;
import nic.testproject.accountingsystem.repositories.contracts.ContractRepository;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractPhaseProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractWithPhasesProjection;
import nic.testproject.accountingsystem.services.reports.ContractReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractReportServiceTests {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractCounterpartyRepository contractCounterpartyRepository;

    @InjectMocks
    private ContractReportService contractReportService;

    @Test
    public void getAllContractsByPeriod_WithValidDates_ReturnsAllContracts() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        List<ContractProjection> contracts = createMockContractProjections();
        List<ContractCounterpartiesProjection> contractCounterparties = createMockContractCounterpartiesProjections();
        when(contractRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(contracts);
        when(contractCounterpartyRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(contractCounterparties);

        AllContracts result = contractReportService.getAllContractsByPeriod(startDate, endDate);

        assertEquals(contracts, result.getContracts());
        assertEquals(contractCounterparties, result.getContractCounterparties());
        verify(contractRepository).findByPlannedStartDateBetween(startDate, endDate);
        verify(contractCounterpartyRepository).findByPlannedStartDateBetween(startDate, endDate);
    }

    @Test
    public void getAllContractsByPeriod_WithEndDateBeforeStartDate_ThrowsConflictException() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 12, 31);

        assertThrows(ConflictException.class, () -> contractReportService.getAllContractsByPeriod(startDate, endDate));
        verifyNoInteractions(contractRepository);
        verifyNoInteractions(contractCounterpartyRepository);
    }

    @Test
    public void getAllContractsByPeriod_WithEmptyContracts_ThrowsResourceNotFoundException() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        when(contractRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> contractReportService.getAllContractsByPeriod(startDate, endDate));
        verify(contractRepository).findByPlannedStartDateBetween(startDate, endDate);
    }

    @Test
    public void getAllContractsByPeriod_WithEmptyContractCounterparties_ThrowsResourceNotFoundException() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        List<ContractProjection> contracts = createMockContractProjections();
        when(contractRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(contracts);
        when(contractCounterpartyRepository.findByPlannedStartDateBetween(startDate, endDate)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> contractReportService.getAllContractsByPeriod(startDate, endDate));
        verify(contractRepository).findByPlannedStartDateBetween(startDate, endDate);
        verify(contractCounterpartyRepository).findByPlannedStartDateBetween(startDate, endDate);
    }

    @Test
    public void getAllPhasesByContract_WithValidContractName_ReturnsContractPhases() {
        Long id = 1L;
        ContractWithPhasesProjection contract = createMockContractWithPhasesProjection();
        when(contractRepository.findContractById(id)).thenReturn(Optional.of(contract));

        List<ContractPhaseProjection> result = contractReportService.getAllPhasesByContract(id);

        assertEquals(contract.getPhases(), result);
        verify(contractRepository).findContractById(id);
    }

    @Test
    public void getAllPhasesByContract_WithInvalidContractName_ThrowsResourceNotFoundException() {
        Long id = 1L;
        when(contractRepository.findContractById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contractReportService.getAllPhasesByContract(id));
        verify(contractRepository).findContractById(id);
    }

    private ContractWithPhasesProjection createMockContractWithPhasesProjection() {
        return () -> null;
    }

    private List<ContractProjection> createMockContractProjections() {
        ContractProjection contracts = new ContractProjection() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public ContractType getType() {
                return null;
            }

            @Override
            public LocalDate getPlannedStartDate() {
                return null;
            }

            @Override
            public LocalDate getPlannedEndDate() {
                return null;
            }

            @Override
            public LocalDate getActualStartDate() {
                return null;
            }

            @Override
            public LocalDate getActualEndDate() {
                return null;
            }

            @Override
            public Double getAmount() {
                return null;
            }
        };
        return Collections.singletonList(contracts);
    }

    private List<ContractCounterpartiesProjection> createMockContractCounterpartiesProjections() {
        ContractCounterpartiesProjection contracts = new ContractCounterpartiesProjection() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Double getAmount() {
                return null;
            }

            @Override
            public LocalDate getPlannedStartDate() {
                return null;
            }

            @Override
            public LocalDate getPlannedEndDate() {
                return null;
            }

            @Override
            public LocalDate getActualStartDate() {
                return null;
            }

            @Override
            public LocalDate getActualEndDate() {
                return null;
            }

            @Override
            public ContractType getType() {
                return null;
            }

            @Override
            public ContractProjection getContract() {
                return null;
            }

        };
        return Collections.singletonList(contracts);
    }

}
