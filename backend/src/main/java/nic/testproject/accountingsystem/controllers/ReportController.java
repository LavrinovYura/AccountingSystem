package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.report.ReportDates;
import nic.testproject.accountingsystem.models.contracts.Contract;
import nic.testproject.accountingsystem.services.reports.ContractReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
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
            @RequestBody ReportDates reportDates
    ) throws IOException {
        LocalDate plannedStartDate = reportDates.getPlannedStartDate();
        LocalDate plannedEndDate = reportDates.getPlannedEndDate();

        List<Contract> contracts = reportService.getContractsByPeriod(plannedStartDate, plannedEndDate);
        ByteArrayInputStream in = reportService.generateExcelReport(contracts);

        byte[] bytes = new byte[in.available()];
        int read = in.read(bytes);
        while (read != -1) {
            read = in.read(bytes);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=contracts_report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(bytes));
    }
}