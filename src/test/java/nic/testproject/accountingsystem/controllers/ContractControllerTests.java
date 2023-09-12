package nic.testproject.accountingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartiesDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractCounterpartyDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhaseDTO;
import nic.testproject.accountingsystem.dtos.contracts.ContractPhasesDTO;
import nic.testproject.accountingsystem.dtos.contracts.UpdateContractDTO;
import nic.testproject.accountingsystem.models.contracts.ContractType;
import nic.testproject.accountingsystem.services.contracts.ContractService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractController.class)
class ContractControllerTests {

    @MockBean
    private ContractService contractService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user")
    public void testSaveContract_ReturnsOk() throws Exception {
        ContractDTO contractDTO = createContractDTO();

        when(contractService.saveContract(any())).thenReturn(contractDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/contracts/saveContract")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(contractDTO))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(contractDTO.getName()));

        verify(contractService, times(1)).saveContract(contractDTO);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testGetContracts_ReturnsOk() throws Exception {
        ContractCriteriaDTO criteria = new ContractCriteriaDTO();

        Set<ContractDTO> contractDTOs = new HashSet<>();

        when(contractService.getContracts(any(), any())).thenReturn(contractDTOs);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/contracts/showContracts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(criteria))
                                .param("page", "0")
                                .param("size", "50")
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).getContracts(criteria, PageRequest.of(0,50));

    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testUpdateContract_ReturnsOk() throws Exception {
        Long id = 1L;
        UpdateContractDTO updateContractDTO = createUpdateContractDTO();
        ContractDTO contractDTO = createContractDTO();
        when(contractService.updateContract(any(UpdateContractDTO.class), any(Long.class))).thenReturn(contractDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/menu/contracts/" + id + "/updateContract")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(contractDTO))
                                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contractDTO.getId()));

        verify(contractService, times(1)).updateContract(updateContractDTO, id);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testDeleteContract_ReturnsOk() throws Exception {
        Long id = 1L;

        doNothing().when(contractService).deleteContract(any(Long.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/menu/contracts/" + id + "/deleteContract")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).deleteContract(id);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testAddPhases_ReturnsOk() throws Exception {
        Long id = 1L;
        Set<ContractPhaseDTO> contractPhaseDTOS = createContractPhaseDTOSet();
        ContractPhasesDTO contractPhasesDTO = new ContractPhasesDTO();
        contractPhasesDTO.setContractPhases(contractPhaseDTOS);

        when(contractService.addContractPhases(any(), eq(id))).thenReturn(contractPhaseDTOS);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/contracts/" + id + "/addPhases")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(contractPhasesDTO))
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).addContractPhases(contractPhaseDTOS, id);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testAddContractCounterparty_ReturnsOk() throws Exception {
        Long id = 1L;
        Set<ContractCounterpartyDTO> contractCounterpartyDTO = createContractCounterpartyDTOSet();
        ContractCounterpartiesDTO contractCounterpartiesDTO = new ContractCounterpartiesDTO();
        contractCounterpartiesDTO.setContractCounterparties(contractCounterpartyDTO);

        when(contractService.addContractCounterparty(any(), eq(id))).thenReturn(contractCounterpartyDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/contracts/" + id + "/addContractCounterparties")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(contractCounterpartiesDTO))
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).addContractCounterparty(contractCounterpartyDTO, id);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testUpdateContractPhase_ReturnsOk() throws Exception {
        Long phaseId = 1L;
        ContractPhaseDTO phaseDTO = createContractPhaseDTO();
        when(contractService.updateContractPhase(any(ContractPhaseDTO.class),any(Long.class))).thenReturn(phaseDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/contracts/" + phaseId + "/updatePhase")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(phaseDTO))
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(contractService, times(1)).updateContractPhase(phaseDTO, phaseId);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testUpdateContractCounterparty_ReturnsOk() throws Exception {
        Long contractCounterpartyId = 1L;
        ContractCounterpartyDTO contractCounterpartyDTO = createContractCounterpartyDTO();
        when(contractService.updateContractCounterparty(any(ContractCounterpartyDTO.class),any(Long.class))).thenReturn(contractCounterpartyDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/contracts/" + contractCounterpartyId + "/updateContractCounterparty")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(contractCounterpartyDTO))
                                .with(csrf()))
                .andExpect(status().isOk());
        verify(contractService, times(1)).updateContractCounterparty(contractCounterpartyDTO, contractCounterpartyId);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testDeleteContractCounterparty_ReturnsOk() throws Exception {
        Long contractCounterpartyId = 1L;
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/menu/contracts/" + contractCounterpartyId + "/deleteContractCounterparty")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk());
        verify(contractService, times(1)).deleteContractCounterparty(contractCounterpartyId);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "admin")
    public void testDeletePhase_ReturnsOk() throws Exception {
        Long phaseId = 1L;
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/menu/contracts/" + phaseId + "/deletePhase")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andExpect(status().isOk());
        verify(contractService, times(1)).deletePhase(phaseId);
    }
    public UpdateContractDTO createUpdateContractDTO() {
        UpdateContractDTO contractDTO = new UpdateContractDTO();
        contractDTO.setName("Contract Name");
        contractDTO.setType(ContractType.DELIVERY);
        contractDTO.setPlannedStartDate(LocalDate.of(2023, 8, 10));
        contractDTO.setPlannedEndDate(LocalDate.of(2023, 9, 10));
        contractDTO.setActualStartDate(LocalDate.of(2023, 8, 15));
        contractDTO.setActualEndDate(LocalDate.of(2023, 9, 5));
        contractDTO.setAmount(new BigDecimal("10000.00"));
        return contractDTO;
    }

    public ContractDTO createContractDTO() {
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setName("Contract Name");
        contractDTO.setType(ContractType.DELIVERY);
        contractDTO.setPlannedStartDate(LocalDate.of(2023, 8, 10));
        contractDTO.setPlannedEndDate(LocalDate.of(2023, 9, 10));
        contractDTO.setActualStartDate(LocalDate.of(2023, 8, 15));
        contractDTO.setActualEndDate(LocalDate.of(2023, 9, 5));
        contractDTO.setAmount(new BigDecimal("10000.00"));

        contractDTO.setContractCounterparties(createContractCounterpartyDTOSet());
        contractDTO.setPhases(createContractPhaseDTOSet());
        return contractDTO;
    }
    public Set<ContractCounterpartyDTO> createContractCounterpartyDTOSet() {
        Set<ContractCounterpartyDTO> contractCounterparties = new HashSet<>();
        contractCounterparties.add(createContractCounterpartyDTO());
        return contractCounterparties;
    }

    public ContractCounterpartyDTO createContractCounterpartyDTO(){
        ContractCounterpartyDTO contractCounterpartyDTO = new ContractCounterpartyDTO();
        contractCounterpartyDTO.setName("Contract Name");
        contractCounterpartyDTO.setType(ContractType.DELIVERY);
        contractCounterpartyDTO.setPlannedStartDate(LocalDate.of(2023, 8, 10));
        contractCounterpartyDTO.setPlannedEndDate(LocalDate.of(2023, 9, 10));
        contractCounterpartyDTO.setActualStartDate(LocalDate.of(2023, 8, 15));
        contractCounterpartyDTO.setActualEndDate(LocalDate.of(2023, 9, 5));
        contractCounterpartyDTO.setAmount(new BigDecimal("10000.00"));
        contractCounterpartyDTO.setCounterpartyId(1L);
        return contractCounterpartyDTO;
    }
    public Set<ContractPhaseDTO> createContractPhaseDTOSet(){
        Set<ContractPhaseDTO> contractPhases = new HashSet<>();
        contractPhases.add(createContractPhaseDTO());

        return contractPhases;
    }
    public ContractPhaseDTO createContractPhaseDTO(){
        ContractPhaseDTO contractPhaseDTO = new ContractPhaseDTO();
        contractPhaseDTO.setId(1L);
        contractPhaseDTO.setName("Phase Name");
        contractPhaseDTO.setPlannedStartDate(LocalDate.of(2023, 8, 1));
        contractPhaseDTO.setPlannedEndDate(LocalDate.of(2023, 8, 31));
        contractPhaseDTO.setPlannedMaterialCosts(BigDecimal.valueOf(5000.00));
        contractPhaseDTO.setPhaseCost(BigDecimal.valueOf(10000.00));
        contractPhaseDTO.setPlannedSalaryExpenses(BigDecimal.valueOf(5000.00));
        contractPhaseDTO.setActualStartDate(LocalDate.of(2023, 8, 1));
        contractPhaseDTO.setActualEndDate(LocalDate.of(2023, 8, 31));
        contractPhaseDTO.setActualMaterialCosts(BigDecimal.valueOf(5500.00));
        contractPhaseDTO.setActualSalaryExpenses(BigDecimal.valueOf(5500.00));
        return contractPhaseDTO;
    }


}
