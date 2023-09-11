package nic.testproject.accountingsystem.controllers;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.report.AllContracts;
import nic.testproject.accountingsystem.dtos.report.RequestDates;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractPhaseProjection;
import nic.testproject.accountingsystem.services.reports.ContractReportService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(allowCredentials = "true", originPatterns = "*")
@RestController
@RequestMapping("api/menu/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ContractReportService reportService;

    @PostMapping("/contracts")
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

    @GetMapping("{id}/contractPhases")
    public ResponseEntity<ByteArrayResource> getPhasesReport(
            @PathVariable Long id
    ) throws IOException {

        List<ContractPhaseProjection> contractPhases = reportService.getAllPhasesByContract(id);
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