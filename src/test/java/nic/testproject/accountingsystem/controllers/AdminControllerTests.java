package nic.testproject.accountingsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nic.testproject.accountingsystem.dtos.administration.RequestRole;
import nic.testproject.accountingsystem.services.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@WithMockUser(authorities = {"ADMIN", "USER"}, username = "admin")
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetUsers_ReturnsSetOfUsers() throws Exception {

        when(adminService.getUsers(any())).thenReturn(new HashSet<>());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/menu/administration/users?page=0&size=50")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                ).andExpect(MockMvcResultMatchers.status().isOk());
        verify(adminService, times(1)).getUsers(PageRequest.of(any(int.class), any(int.class)));
    }

    @Test
    public void testDeleteUser_ReturnsNoContent() throws Exception {

        doNothing().when(adminService).deleteUser(any(Long.class));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/menu/administration/users/1/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(adminService, times(1)).deleteUser(any(Long.class));
    }

    @Test
    public void testAddRole_ReturnsNoContent() throws Exception {
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType("ADMIN");
        Long personId = 1L;

        doNothing().when(adminService).addRole(any(String.class), any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/menu/administration/users/1/addRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRole))
                        .with(csrf())
                ).andExpect(status().isNoContent());

        // Проверяем, что adminService.addRole был вызван один раз
        verify(adminService, times(1)).addRole(requestRole.getRoleType(), personId);
    }

    @Test
    public void testRemoveRole_ReturnsNoContent() throws Exception {
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType("ADMIN");
        Long personId = 1L;

        doNothing().when(adminService).removeRole(any(), any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/menu/administration/users/1/removeRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRole))
                        .with(csrf())
                ).andExpect(status().isNoContent());

        verify(adminService, times(1)).removeRole(requestRole.getRoleType(), personId);
    }

    @Test
    public void testGetUsersByRole_ReturnsListOfUsers() throws Exception {
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType("ADMIN");

        Pageable pageable = PageRequest.of(0, 50);

        when(adminService.getUsersByRole(any(), any())).thenReturn(new HashSet<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/menu/administration/users/usersByRole?page=0&size=50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRole))
                        .with(csrf())
                )
                .andExpect(status().isOk());

        verify(adminService, times(1)).getUsersByRole(pageable, requestRole.getRoleType());
    }
}

