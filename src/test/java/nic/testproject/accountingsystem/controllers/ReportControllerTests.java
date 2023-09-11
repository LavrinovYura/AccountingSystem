package nic.testproject.accountingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nic.testproject.accountingsystem.dtos.report.AllContracts;
import nic.testproject.accountingsystem.dtos.report.RequestDates;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractCounterpartiesProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractPhaseProjection;
import nic.testproject.accountingsystem.repositories.contracts.projections.ContractProjection;
import nic.testproject.accountingsystem.services.reports.ContractReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
public class ReportControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ContractReportService reportService;

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user")
    void testGetContractsReport_ReturnsExcelFile() throws Exception {
        RequestDates reportDates = new RequestDates();

        List<ContractProjection> contracts = new ArrayList<>();
        List<ContractCounterpartiesProjection> contractCounterparties = new ArrayList<>();
        AllContracts allContracts = new AllContracts(contracts, contractCounterparties);

        ByteArrayOutputStream excelData = new ByteArrayOutputStream();

        when(reportService.getAllContractsByPeriod(any(), any())).thenReturn(allContracts);
        when(reportService.generateContractsExcelReport(allContracts)).thenReturn(excelData);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/reports/contracts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(reportDates))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contracts_report.xlsx"))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user")
    void testGetPhasesReport_ReturnsExcelFile() throws Exception {

        Long contractId = 1L;
        List<ContractPhaseProjection> contractPhases = new ArrayList<>();

        ByteArrayOutputStream excelData = new ByteArrayOutputStream();

        when(reportService.getAllPhasesByContract(contractId)).thenReturn(contractPhases);
        when(reportService.generatePhasesExcelReport(contractPhases)).thenReturn(excelData);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/menu/reports/" + contractId + "/contractPhases", contractId)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=phases_report.xlsx"))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
    }
}


