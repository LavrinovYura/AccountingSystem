package nic.testproject.accountingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nic.testproject.accountingsystem.dtos.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.dtos.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.services.contracts.CounterpartyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CounterpartyController.class)
class CounterpartyControllerTests {

    @MockBean
    private CounterpartyService counterpartyService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user")
    void testSaveCounterparty_ReturnsOkResponse() throws Exception {
        CounterpartyDTO counterpartyDTO = createCounterpartyDTO();

        when(counterpartyService.saveCounterparty(any())).thenReturn(counterpartyDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/counterparties/save")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(counterpartyDTO))
                                .with(csrf()))
                .andExpect(status().isCreated());

        verify(counterpartyService, times(1)).saveCounterparty(counterpartyDTO);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user")
    void testUpdateCounterparty_ReturnsOkResponse() throws Exception {
        Long id = 1L;
        CounterpartyDTO counterpartyDTO = createCounterpartyDTO();

        when(counterpartyService.updateCounterparty(any(), any(Long.class))).thenReturn(counterpartyDTO);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/menu/counterparties/" + id + "/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(counterpartyDTO))
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(counterpartyService, times(1)).updateCounterparty(counterpartyDTO, id);
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user")
    void testShowCounterparties_ReturnsOkResponse() throws Exception {
        CounterpartyDTO criteria = createCounterpartyDTO();
        Set<CounterpartyDTO> counterparties = new HashSet<>();
        when(counterpartyService.findCounterparties(any(), any())).thenReturn(counterparties);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/counterparties/show")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(criteria))
                                .param("page", "0")
                                .param("size", "50")
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(counterpartyService, times(1)).findCounterparties(criteria, PageRequest.of(0, 50));
    }


    @Test
    @WithMockUser(authorities = {"USER"}, username = "user")
    void testDeleteCounterparty_ReturnsOkResponse() throws Exception {
        Long id = 1L;

        doNothing().when(counterpartyService).deleteCounterparty(any(Long.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/menu/counterparties/" + id + "/delete")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(id))
                                .with(csrf()))
                .andExpect(status().isOk());

        verify(counterpartyService, times(1)).deleteCounterparty(id);
    }

    public CounterpartyDTO createCounterpartyDTO() {
        CounterpartyDTO counterpartyDTO = new CounterpartyDTO();
        counterpartyDTO.setAddress("address");
        counterpartyDTO.setId(1L);
        counterpartyDTO.setInn("3333-333");
        counterpartyDTO.setName("counterparty");
        return counterpartyDTO;
    }

}
