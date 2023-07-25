package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.report.AllContracts;
import nic.testproject.accountingsystem.dto.report.RequestDates;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractPhaseProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractProjection;
import nic.testproject.accountingsystem.services.reports.ContractReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTests {

    @Mock
    private ContractReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @Test
    public void testGetContractsReport() throws Exception {
        // Arrange
        RequestDates dates = new RequestDates();
        LocalDate plannedStartDate = null;
        LocalDate plannedEndDate = null;

        List<ContractProjection> contracts = new ArrayList<>();
        // Set up contracts data

        List<ContractCounterpartiesProjection> contractCounterparties = new ArrayList<>();
        // Set up contract counterparties data

        AllContracts allContracts = new AllContracts(contracts, contractCounterparties);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Set up the expected output stream

        when(reportService.getAllContractsByPeriod(plannedStartDate, plannedEndDate)).thenReturn(allContracts);
        when(reportService.generateContractsExcelReport(allContracts)).thenReturn(outputStream);

        // Act
        ResponseEntity<ByteArrayResource> response = reportController.getContractsReport(dates);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=contracts_report.xlsx", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertArrayEquals(outputStream.toByteArray(), Objects.requireNonNull(response.getBody()).getByteArray());

        verify(reportService).getAllContractsByPeriod(plannedStartDate, plannedEndDate);
        verify(reportService).generateContractsExcelReport(allContracts);
    }


    @Test
    public void testGetPhasesReport() throws Exception {
        // Arrange
        RequestName contractName = new RequestName();

        List<ContractPhaseProjection> contractPhases = new ArrayList<>();
        // Set up contract phases data

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Set up the expected output stream

        when(reportService.getAllPhasesByContract(contractName.getName())).thenReturn(contractPhases);
        when(reportService.generatePhasesExcelReport(contractPhases)).thenReturn(outputStream);

        // Act
        ResponseEntity<ByteArrayResource> response = reportController.getPhasesReport(contractName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=phases_report.xlsx", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertArrayEquals(outputStream.toByteArray(), Objects.requireNonNull(response.getBody()).getByteArray());

        verify(reportService).getAllPhasesByContract(contractName.getName());
        verify(reportService).generatePhasesExcelReport(contractPhases);
    }
}


