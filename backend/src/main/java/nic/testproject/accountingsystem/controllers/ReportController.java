package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.report.AllContracts;
import nic.testproject.accountingsystem.dto.report.RequestDates;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.exceptions.RestExceptionHandler;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractPhaseProjection;
import nic.testproject.accountingsystem.services.reports.ContractReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/menu/reports")
public class ReportController {

    private final ContractReportService reportService;

    @Autowired
    public ReportController(ContractReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/contracts")
    public ResponseEntity<ByteArrayResource> getContractsReport(
            @RequestBody RequestDates reportDates
    ) throws IOException {
        LocalDate plannedStartDate = reportDates.getPlannedStartDate();
        LocalDate plannedEndDate = reportDates.getPlannedEndDate();

        AllContracts contracts = reportService.getAllContractsByPeriod(plannedStartDate, plannedEndDate);
        ByteArrayOutputStream outputStream = reportService.generateContractsExcelReport(contracts);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=contracts_report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(outputStream.toByteArray()));
    }

    @GetMapping("/contractPhases")
    public ResponseEntity<ByteArrayResource> getPhasesReport(
            @RequestBody RequestName name
    ) throws IOException {

        List<ContractPhaseProjection> contractPhases = reportService.getAllPhasesByContract(name.getName());
        ByteArrayOutputStream outputStream = reportService.generatePhasesExcelReport(contractPhases);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=phases_report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(outputStream.toByteArray()));
    }
}